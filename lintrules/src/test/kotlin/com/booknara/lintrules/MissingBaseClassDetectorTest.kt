package com.booknara.lintrules

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.booknara.lintrules.detector.ISSUE_MISSING_BASE_CLASS
import org.junit.Test

class MissingBaseClassDetectorTest {
    private val activityStub = kotlin(
        """
        package androidx.appcompat.app
        
        class AppCompatActivity
        
    """
    ).indented()

    private val fragmentStub = kotlin(
        """
        package androidx.fragment.app

        class Fragment

    """
    ).indented()

    // Step 1
    @Test
    fun `when the activity has base activity no error is given`() {
        TestLintTask.lint().files(activityStub, kotlin(
            """
            package com.booknara.android.apps.lint.ui
            
            import androidx.appcompat.app.AppCompatActivity

            class SomeActivity : BaseActivity()

            class BaseActivity : AppCompatActivity()
            
            class MissingBaseActivity : AppCompatActivity()
        """
        ).indented())
            .issues(ISSUE_MISSING_BASE_CLASS)
            .allowMissingSdk(true)
            .run()
            .expectErrorCount(1)
    }


    // Step 2
    @Test
    fun `when a class is not an activity no error is given`() {
        TestLintTask.lint().files(kotlin(
                """
            package test

            class SomeClass
        """
            ).indented())
            .issues(ISSUE_MISSING_BASE_CLASS)
            .allowMissingSdk(true)
            .run()
            .expectClean()
    }

    // Step 3
    @Test
    fun `when the activity does not have base activity error is given`() {
        TestLintTask.lint().files(activityStub, kotlin(
            """
            package test
            
            import androidx.appcompat.app.AppCompatActivity

            class SomeActivity : AppCompatActivity()
        """
        ).indented())
            .issues(ISSUE_MISSING_BASE_CLASS)
            .allowMissingSdk(true)
            .run()
            .expectErrorCount(1)
    }

    // Step 4
    @Test
    fun `when the parent of a fragment is not the support fragment an error is given`() {
        TestLintTask.lint().files(fragmentStub, kotlin(
            """
            package com.booknara.android.apps.lint.ui
            
            import androidx.fragment.app.Fragment

            class SomeFragment : BaseFragment()

            class BaseFragment : Fragment()
            
            class MissingBaseFragment : Fragment()
        """
        ).indented())
            .issues(ISSUE_MISSING_BASE_CLASS)
            .allowMissingSdk(true)
            .run()
            .expectErrorCount(1)
    }
}