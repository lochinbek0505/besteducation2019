// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.besteducation2019.model

data class course_model (
    val data: Data,
    val errors: Errors,
    val status: String
)

data class Data (
    val courses: List<Course>
)


class Errors()
