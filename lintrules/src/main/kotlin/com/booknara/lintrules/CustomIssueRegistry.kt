package com.booknara.lintrules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.booknara.lintrules.detector.ISSUE_MISSING_BASE_CLASS
import com.booknara.lintrules.detector.ISSUE_MISSING_VIEWMODEL_DETECTOR
import com.booknara.lintrules.detector.ISSUE_VIEW_STATE

class CustomIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
            ISSUE_MISSING_BASE_CLASS,
            // ISSUE_USER_INTERFACE_INHERITANCE,
            ISSUE_VIEW_STATE,
            // ISSUE_MISSING_ROBOT_DETECTOR,
            ISSUE_MISSING_VIEWMODEL_DETECTOR
        )

    override val api: Int = CURRENT_API
}
