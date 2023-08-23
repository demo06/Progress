package com.zgw.plugin


import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.IOException


class PgyerUpload : Plugin<Project> {
    private val okHttpClient = OkHttpClient()
    private val uploadUrl = "https://www.pgyer.com/apiv2/app/upload"
    override fun apply(project: Project) {
        project.task("upload") { task ->
            task.doLast {
                val apkPath =
                    "${project.buildDir.absolutePath}${File.separator}outputs${File.separator}apk${File.separator}release"
                val apk = findFile(File(apkPath))
                project.logger.warn("apkPath:$apkPath")
                apk?.let { uploadApk(it, project) }
            }.dependsOn("assembleRelease")
        }
    }

    /**
     * 查找编译目录下的apk文件
     *
     * @param path apk生成目录
     * @return 找到的apk文件
     */
    private fun findFile(path: File): File? {
        if (path.exists() && path.isDirectory) {
            val files = path.listFiles { _, name ->
                name.endsWith("apk")
            }
            if (files != null) {
                if (files.isNotEmpty()) {
                    return files[0]
                }
            }
        }
        return null
    }

    /**
     * 上传apk到蒲公英
     *
     * @param file
     */
    private fun uploadApk(file: File, project: Project) {
        val body = file.asRequestBody("multipart/form-data".toMediaType())
        val formBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("_api_key", "1a3d9ecd5894062627c2f8408ab27a07")
            .addFormDataPart("file", file.name, body)
            .build()
        val request = Request.Builder()
            .url(uploadUrl)
            .post(formBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                project.logger.warn("上传失败")
            }

            override fun onResponse(call: Call, response: Response) {
                project.logger.warn("msg:${response.message}code:${response.code}")
            }

        })
    }

}