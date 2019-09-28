package com.booknara.lintrules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.booknara.lintrules.detector.*

class CustomIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
            ISSUE_BIG_SIZED_BITMAP_IMAGE,
            ISSUE_MISSING_BASE_CLASS,
            ISSUE_INCORRECT_IMPORT,
            // ISSUE_USER_INTERFACE_INHERITANCE,
            ISSUE_VIEW_STATE,
            // ISSUE_MISSING_ROBOT_DETECTOR,
            ISSUE_MISSING_VIEWMODEL_DETECTOR
        )

    override val api: Int = CURRENT_API
    override val minApi: Int = 1
}
