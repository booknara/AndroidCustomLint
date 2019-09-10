package com.booknara.lintrules.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UImportStatement
import java.util.*

val ISSUE_INCORRECT_IMPORT = Issue.create(
        id = "InvalidAnnotationImport",
        briefDescription = "Invalid annotation package classes used",
        explanation = IncorrectImportDetector.MESSAGE,
        category = Category.CORRECTNESS,
        priority = 6,
        severity = Severity.ERROR,
        implementation = Implementation(
                IncorrectImportDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE))
)

class IncorrectImportDetector : Detector(), Detector.UastScanner {
    companion object {
        const val MESSAGE = "Incorrect import classes is being used. Please check it out whether it is allowed or not"
        val INVALID_FRAMEWORK_ANNOTATION_PACKAGE =  listOf(
                "android.support.annotation.NonNull",
                "android.support.annotation.Nullable"
                )
    }

    override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        // Note: Visiting UAST nodes is a pretty general purpose mechanism;
        // Lint has specialized support to do common things like "visit every class
        // that extends a given super class or implements a given interface", and
        // "visit every call site that calls a method by a given name" etc.
        // Take a careful look at UastScanner and the various existing lint check
        // implementations before doing things the "hard way".
        // Also be aware of context.getJavaEvaluator() which provides a lot of
        // utility functionality.
        return object : UElementHandler() {
            override fun visitImportStatement(node: UImportStatement) {
                node.importReference?.let { reference ->
                    if (INVALID_FRAMEWORK_ANNOTATION_PACKAGE.any { reference.asSourceString().contains(it) }) {
                        context.report(
                                ISSUE_INCORRECT_IMPORT, node,
                                context.getLocation(reference),
                                MESSAGE
                        )
                    }
                }
            }
        }
    }
}