package com.widmeyertemplate.modules.domain.model

enum class TypeModel(val srcMain: String, val packageAdditionalName: String) {
    FEATURE(srcMain = "main", packageAdditionalName = "screen"),
    SHARED(srcMain = "commonMain", packageAdditionalName = "features")
}
