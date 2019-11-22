package com.hackday.subtysis

import com.hackday.subtysis.model.LangCode
import com.hackday.subtysis.model.Subtitle
import java.io.File


open class SubtitleParserImpl : SubtitleParser {

    override fun createSubtitle(filename: String): ArrayList<Subtitle> {
        val bufferReader = File(filename)
        val lineList = mutableListOf<String>()
        val arrayList = ArrayList<Subtitle>()
        var count: Int = 0
        var m = -1
        bufferReader.useLines { lines -> lines.forEach { lineList.add(it) } }
        lineList.forEach {
            var s: String = it
            if (filename.contains("SRT") || filename.contains("srt"))//SRT 형식의 자막 파일을 처리하는 과정
            {
                if (s.contains("-->"))//시간정보를 추출하는 단계입니다.
                {
                    val temp = Subtitle()
                    m = m + 1
                    arrayList.add(temp)
                    while (s.contains(":"))
                        s = s.replace(":", "")

                    while (s.contains(","))
                        s = s.replace(",", "")
                    var hour: String = s.substring(0, 2)
                    var minute: String = s.substring(2, 4)
                    var second: String = s.substring(4, 6)
                    var time =
                        Integer.valueOf(hour) * 3600 + Integer.valueOf(minute) * 60 + Integer.valueOf(
                            second
                        )
                    arrayList[m].frame = time
                }
                if (s != "") {
                    if (s[0] !in '0'..'9') {
                        if (s.contains("</font>")) {
                            s = s.replace("</font>", " ")
                        }
                        while (s.contains("ont"))
                        //글자 정보 태그는 삭제합니다.
                        {
                            var ind = 0
                            //글자 정보 태그를 찾는 과정입니다.
                            for (i in s.indexOf("ont") until s.length) {
                                if (s[i] == '>') {
                                    ind = i
                                    break
                                }
                            }
                            var cr = s.substring(s.indexOf("ont") - 2, ind + 1)
                            s = s.replace(cr, " ")//font 태그 제거 작업
                        }

                        if (s.matches(".[ㄱ-ㅎ ㅏ-ㅣ 가-힣]+.*".toRegex())) {
                            if (m >= 0)
                                arrayList[m].langCode = LangCode.KO
                        } else {
                            if (m >= 0)
                                arrayList[m].langCode = LangCode.EN
                        }
                        //자막내용을 추가하는 단계
                        if (s !== "") {
                            if (m >= 0) {
                                arrayList[m].sentence = arrayList[m].sentence + s
                            }
                        }
                    }
                }
            }
            //SMI 파일을 처리하는 단계
            else if (filename.contains("smi") || filename.contains("SMI")) {
                if (s.contains("<BODY>"))
                    count = 1
                else if (s.contains("</BODY>"))
                    count = 0
                if (count == 1 && !s.contains("&nbsp")) {
                    while (s.contains("ont"))
                    //글자 정보 태그는 삭제합니다.
                    {
                        var ind = 0
                        //글자 정보 태그를 찾는 과정입니다.
                        for (i in s.indexOf("ont") until s.length) {
                            if (s[i] == '>') {
                                ind = i
                                break
                            }
                        }
                        var cr = s.substring(s.indexOf("ont") - 2, ind + 1)
                        s = s.replace(cr, " ")//font 태그 제거 작업
                    }
                    if (s.contains("<br>"))
                    //줄 띄움 태그 역시 삭제합니다.
                    {
                        s = s.replace("<br>", " ")
                    }
                    if (s.contains("</font>")) {
                        s = s.replace("</font>", " ")
                    }
                    if (s.contains("SYNC"))
                    //SYNC 태그에서 프레임 번호를 가져오는 작업입니다.
                    {
                        val temp = Subtitle()
                        m = m + 1
                        arrayList.add(temp)
                        var st = 0
                        var endp = 0
                        //프레임번호를 찾는 과정입니다.
                        for (i in s.indexOf("SYNC") until s.length) {
                            if (s[i] == '=')
                                st = i + 1
                            else if (s[i] == '>') {
                                endp = i
                                break
                            }
                        }
                        val cr = s.substring(st, endp)
                        if (cr.isNotBlank()) {
                            val time = Integer.valueOf(cr)
                            arrayList[m].frame = time
                        }
                        val er = s.substring(s.indexOf("<"), s.indexOf(">") + 1)
                        s = s.replace(er, "")
                    }
                    if (s.contains("P Class"))
                    //P class 태그에서 자막의 type를 가져오는 작업입니다.
                    {
                        if (s.contains("KRCC")) {
                            arrayList[m].langCode = LangCode.KO
                        } else {
                            arrayList[m].langCode = LangCode.EN
                        }
                        val er = s.substring(s.indexOf("<"), s.indexOf(">") + 1)
                        s = s.replace(er, "")
                    }

                    if (s !== "") {
                        if (m >= 0) {
                            arrayList[m].sentence = arrayList[m].sentence + s
                        }
                    }
                }
            }
        }
        return (arrayList)
    }
}

