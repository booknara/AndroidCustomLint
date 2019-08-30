package com.booknara.lintrules.detector

import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UField
import java.util.*


val ISSUE_VIEW_STATE = Issue.create(
    id = "ViewState",
    briefDescription = ViewStateDetector.MESSAGE,
    explanation = ViewStateDetector.MESSAGE,
    category = Category.CORRECTNESS,
    priority = 5,
    severity = Severity.ERROR,
    implementation = Implementation(
        ViewStateDetector::class.java,
        EnumSet.of(Scope.JAVA_FILE)))


class ViewStateDetector : Detector(), Detector.UastScanner {

    companion object {
        const val MESSAGE = "Please keep state out of UI classes"
        private val UI_CLASSES =  listOf(
            "android.support.v7.app.AppCompatActivity",
            "androidx.appcompat.app.AppCompatActivity",
            "android.support.v4.app.Fragment",
            "androidx.fragment.app.Fragment")

        private const val VIEWMODEL_CLASS = "androidx.lifecycle.ViewModel"
    }

    override fun applicableSuperClasses(): List<String>? =
        UI_CLASSES

    override fun visitClass(context: JavaContext, declaration: UClass) {
        declaration.fields.forEach {
            if (!it.isStatic &&
                !it.isFinal &&
                !isAndroidClass(it) &&
                !isViewModel(it) &&
                !isAdapter(it) &&
                !looksLikeCustomView(it)) {
                context.report(
                    ISSUE_VIEW_STATE, it,
                    context.getLocation(it),
                    MESSAGE
                )
            }
        }
    }

    private fun isViewModel(it: UField) =
        it.type.superTypes.isNotEmpty() && it.type.superTypes[0].canonicalText == VIEWMODEL_CLASS

    private fun isAndroidClass(it: UField) = it.type.canonicalText.startsWith("android")

    private fun isAdapter(it: UField) = it.type.canonicalText.toLowerCase().contains("adapter")

    private fun looksLikeCustomView(it: UField) =
        if (it.type.canonicalText.toLowerCase().contains("button")) {
            true
        } else it.type.canonicalText.toLowerCase().contains("view")
}
