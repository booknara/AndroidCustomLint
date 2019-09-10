package com.booknara.lintrules

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.booknara.lintrules.detector.ISSUE_INCORRECT_IMPORT
import org.junit.Test

class IncorrectImportDetectorTest {
    private val androidStub = java(
            """
        package androidx.appcompat.app;
        
        class AppCompatActivity {}
        
    """
    ).indented()

    // Step 1
    @Test
    fun `when the class has android annotation class error is given`() {
        TestLintTask.lint().files(androidStub, java(
                """
            package com.samsung.android.spay.common.ui;

            import android.support.annotation.Nullable;
            import androidx.appcompat.app.AppCompatActivity;

            class MainActivity extends AppCompatActivity {
                public void setIntent(@Nullable String action) { }
            }
            
        """
        ).indented())
                .issues(ISSUE_INCORRECT_IMPORT)
                .allowMissingSdk(true)
                .run()
                .expectErrorCount(1)
    }

    // Step 2
    @Test
    fun `when the class has android Jetpack annotation class no error is given`() {
        TestLintTask.lint().files(androidStub, java(
                """
            package com.samsung.android.spay.common.ui;

            import androidx.annotation.Nullable;
            import androidx.appcompat.app.AppCompatActivity;
            
            class MainActivity extends AppCompatActivity {
                public void setIntent(@Nullable String action) { }
            }

        """
        ).indented())
                .issues(ISSUE_INCORRECT_IMPORT)
                .allowMissingSdk(true)
                .run()
                .expectClean()
    }

    // Step 3
    @Test
    fun `when the activity does not any annotation classes no error is given`() {
        TestLintTask.lint().files(java(
                """
            package com.samsung.android.spay.common.ui;

            class SomeClass { }
        """
        ).indented())
                .issues(ISSUE_INCORRECT_IMPORT)
                .allowMissingSdk(true)
                .run()
                .expectClean()
    }
}