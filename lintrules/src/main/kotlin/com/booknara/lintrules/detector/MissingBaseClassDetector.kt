package com.booknara.lintrules.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import java.util.*

val ISSUE_MISSING_BASE_CLASS = Issue.create(
    id = "MissingBaseClass",
    briefDescription = "Missing base class required",
    explanation = MissingBaseClassDetector.MESSAGE,
    category = Category.CORRECTNESS,
    priority = 6,
    severity = Severity.ERROR,
    implementation = Implementation(
        MissingBaseClassDetector::class.java,
        EnumSet.of(Scope.JAVA_FILE))
)

class MissingBaseClassDetector : Detector(), Detector.UastScanner {
    companion object {
        const val MESSAGE = "Activity should inherit one of base classes required. Please check your base class whether it inherits the base class"
        private val REQUIRED_APP_BASE_ACTIVITY_CLASS =  listOf(
            "com.booknara.android.apps.lint.ui.BaseActivity")
        private val REQUIRED_APP_BASE_FRAGMENT_CLASS =  listOf(
            "com.booknara.android.apps.lint.ui.BaseFragment")

        private val ANDROID_BASE_ACTIVITY_CLASSES =  listOf(
            "androidx.appcompat.app.AppCompatActivity",
            "androidx.fragment.app.FragmentActivity")
        private val ANDROID_BASE_FRAGMENT_CLASSES =  listOf(
            "androidx.fragment.app.Fragment")
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? = listOf(UClass::class.java)

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
            override fun visitClass(node: UClass) {
                val qualifiedName = context.evaluator.getQualifiedName(node)
//                println("visit qualifiedName name :  $qualifiedName")

                // Skip 1: base class & Android activity/fragment classes
                if (qualifiedName in REQUIRED_APP_BASE_ACTIVITY_CLASS
                    || qualifiedName in ANDROID_BASE_ACTIVITY_CLASSES
                    || qualifiedName in REQUIRED_APP_BASE_FRAGMENT_CLASS
                    || qualifiedName in ANDROID_BASE_FRAGMENT_CLASSES) {
//                    println("Skip 1")
                    return
                }

                // Skip 2: any classes that don't use Android activity/fragment classes
                if (node.supers.none {
                        context.evaluator.getQualifiedName(it) in ANDROID_BASE_ACTIVITY_CLASSES
                        || context.evaluator.getQualifiedName(it) in ANDROID_BASE_FRAGMENT_CLASSES}) {
//                    println("Skip 2")
                    return
                }

                val baseClassFound = node.supers.any {
                    context.evaluator.getQualifiedName(it) in REQUIRED_APP_BASE_ACTIVITY_CLASS
                            || context.evaluator.getQualifiedName(it) in REQUIRED_APP_BASE_FRAGMENT_CLASS}

                if (baseClassFound) {
                    println("base class found")
                } else {
//                    println("base class not found")
                    context.report(
                        ISSUE_MISSING_BASE_CLASS, node,
                        context.getNameLocation(node),
                        MESSAGE
                    )
                }
            }
        }
    }
}