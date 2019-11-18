package com.hackday.subtysis

import com.hackday.subtysis.model.LangCode
import java.io.File
import com.hackday.subtysis.model.Subtitle


open class SubtitleParserImpl : SubtitleParser {

    override fun createSubtitle(filename: String):ArrayList<Subtitle> {
        val bufferReader = File(filename)
        val lineList = mutableListOf<String>()
        val arrayList = ArrayList<Subtitle>()
        var count:Int= 0
        var m = -1
        bufferReader.useLines { lines -> lines.forEach { lineList.add(it)} }
        lineList.forEach{
            var s:String=it
            if(s.contains("<BODY>"))
                count=1
            else if (s.contains("</BODY>"))
                count = 0
            if (!s.contains("&nbsp;") && count == 1)
            {

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
                    var cr = s.substring(s.indexOf("ont") - 2, ind +1)
                    s=s.replace(cr, "")//font 태그 제거 작업
                }
                if (s.contains("<br>"))
                //줄 띄움 태그 역시 삭제합니다.
                {
                    s=s.replace("<br>", "")
                }

                if (s.contains("SYNC"))
                //SYNC 태그에서 프레임 번호를 가져오는 작업입니다.
                {
                    val temp = Subtitle()
                    m=m+1
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
                    val time = Integer.valueOf(cr)
                    arrayList[m].frame = time
                    val er = s.substring(s.indexOf("<"), s.indexOf(">") + 1)
                    s = s.replace(er, "")
                }

                if (s.contains("P Class"))
                //P class 태그에서 자막의 type를 가져오는 작업입니다.
                {
                    var st = 0
                    var endp = 0
                    //자막 언어 타입을 찾는 과정입니다.
                    for (i in s.indexOf("P Class") until s.length) {
                        if (s[i] == '=')
                            st = i + 1
                        else if (s[i] == '>') {
                            endp = i
                            break
                        }
                    }
                    val cr = s.substring(st, endp)
                    if(cr=="KRCC")
                    {
                        arrayList[m].langCode=LangCode.KO
                    }
                    else
                    {
                        arrayList[m].langCode=LangCode.EN
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
        return(arrayList)
    }
}

