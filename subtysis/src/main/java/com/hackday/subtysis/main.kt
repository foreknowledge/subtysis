package com.hackday.subtysis

//createSubtitle 함수가 arraylist를 잘 반환하는지 확인하는 함수
object main : SubtitleParserImpl() {
    @JvmStatic
    fun main(args: Array<String>) {
        name="app\\src\\main\\assets\\치아문난난적소시광(우리의 따뜻했던 시절에게)_23회_NonDRM_[최소화질]_253891057.smi"
        for (i in createSubtitle(name).indices) {
            println(createSubtitle(name)[i].frame)
            println(createSubtitle(name)[i].langCode)
            println(createSubtitle(name)[i].sentence)
        }

    }
}
