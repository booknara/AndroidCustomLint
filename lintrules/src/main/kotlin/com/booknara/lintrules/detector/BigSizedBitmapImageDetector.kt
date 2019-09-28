package com.booknara.lintrules.detector

import com.android.SdkConstants.*
import com.android.tools.lint.checks.IconDetector
import com.android.tools.lint.detector.api.*
import com.android.utils.SdkUtils.endsWithIgnoreCase
import java.io.File
import java.util.*
import kotlin.collections.HashMap

val ISSUE_BIG_SIZED_BITMAP_IMAGE = Issue.create(
    id = "BigSizedBitmapUsed",
    briefDescription = "A big sized PNG or JPEG Bitmap image used",
    explanation = BigSizedBitmapImageDetector.MESSAGE,
    category = Category.ICONS,
    priority = 8,
    severity = Severity.ERROR,
    implementation = Implementation(
        BigSizedBitmapImageDetector::class.java,
        EnumSet.of(Scope.ALL_RESOURCE_FILES)
    )
)

class BigSizedBitmapImageDetector : IconDetector() {
    companion object {
        const val MESSAGE =
            "A big sized Bitmap(png, jpeg) image is being used. Please convert it to WebP image format instead"
        const val MAX_FILE_SIZE_THRESHOLD = 500 * 1024  // 500K
        const val BUILD_SDK_JELLY_BEAN_MR2 = 18         // Android 4.3 (Lossless and transparent WebP image are supported in Android 4.3 - API level 18)
    }

    override fun afterCheckEachProject(context: Context) {
        if (context.mainProject.minSdkVersion.apiLevel < BUILD_SDK_JELLY_BEAN_MR2) {
            println("not eligible to apply the url because lower Sdk version")
            return
        }

        checkResourceFolder(context, context.project)
    }

    private fun checkResourceFolder(context: Context, project: Project) {
        val resourceFolders = project.resourceFolders

        for (res in resourceFolders) {
            val folders = res.listFiles()
            folders.let {
                val fileSizes = HashMap<File, Long>()

                // Collect all the drawable folder information
                for (folder in it) {
                    val folderName = folder.name
                    println("folder name : $folderName")
                    if (folderName.startsWith(DRAWABLE_FOLDER)) {
                        val files = folder.listFiles()
                        if (files != null) {
                            checkDrawableDir(files, fileSizes)
                        }
                    }
                }

                for (entry in fileSizes.entries) {
                    val file = entry.key
                    val name = file.name
                    if (endsWithIgnoreCase(name, DOT_PNG) && !endsWithIgnoreCase(name, DOT_9PNG)
                        || endsWithIgnoreCase(name, DOT_JPG)
                        || endsWithIgnoreCase(name, DOT_JPEG)) {
                        // Launcher icons are not eligible for WEBP conversion
                        val baseName = getBaseName(name)
                        if (isLauncherIcon(baseName) || isAdaptiveIconLayer(baseName)) {
                            println("not eligible because of exceptional icon")
                            continue
                        }

                        val sizeLong = entry.value
                        if (sizeLong > MAX_FILE_SIZE_THRESHOLD) {
                            val location = Location.create(file)
                            context.report(
                                ISSUE_BIG_SIZED_BITMAP_IMAGE,
                                location,
                                MESSAGE
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkDrawableDir(files: Array<File>, fileSizes: MutableMap<File, Long>) {
        for (file in files) {
            val fileName = file.name

            if (endsWith(fileName, DOT_PNG)
                || endsWith(fileName, DOT_JPG)
                || endsWith(fileName, DOT_JPEG)
                || endsWith(fileName, DOT_WEBP)) {
                // Only scan .png files (except 9-patch png's) and jpg files for
                // dip sizes. Duplicate checks can also be performed on ninepatch files.
                fileSizes[file] = file.length()
            }
        }
    }

    private fun isLauncherIcon(name: String): Boolean {
        assert(name.indexOf('.') == -1) { name } // Should supply base name

        // Naming convention
        //noinspection SimplifiableIfStatement
        if (name.startsWith("ic_launcher") && !isAdaptiveIconLayer(name)) {
            return true
        }

        return false
    }

    private fun isAdaptiveIconLayer(name: String): Boolean {
        return name.startsWith("ic_launcher")
                && (name.endsWith("_foreground") || name.endsWith("_background"))
    }
}
