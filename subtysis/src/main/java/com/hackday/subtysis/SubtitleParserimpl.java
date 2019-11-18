package com.hackday.subtysis;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

public class SubtitleParserimpl implements SubtitleParser{
    @Override
    public ArrayList<Subtitle> createSubtitle(String filename) {
        BufferedReader bReader = null;
        ArrayList<Subtitle>arrayList = new ArrayList<Subtitle>();
        //프레임 번호 정보와 자막타입, 자막내용들을 담은 클래스(Subtitle)를 각각의 성분으로 가지는 ARRAYLIST입니다.
        try {
            String s;
            File file = new File(filename);
            bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            int count=0;
            int m=-1;
            //걸러내는 작업을 거친 텍스트 파일을 바탕으로 프레임 정보, 자막의 언어 type, 자막 내용들을 파싱하는  부분입니다.
            // 더이상 읽어들일게 없을 때까지 읽어들이게 합니다.
            while((s = bReader.readLine()) != null){

                if(s.contains("<BODY>"))
                    count=1;
                else if(s.contains("</BODY>"))
                    count=0;
                //태그 body 사이의 내용만을 대상으로 작업합니다.
                if(!s.contains("&nbsp;") && count==1)// &nbsp 가 포함된 문장은 작업 대상에서 제외 합니다
                {
                    if(s.contains("/ont"))//글자 정보 태그는 삭제합니다.
                    {
                        int ind=0;
                        for(int i=s.indexOf("/ont");i<s.length();i++)
                        {
                            if(s.charAt(i)== '>')
                            {
                                ind=i;
                                break;
                            }
                        }
                        String cr = s.substring(s.indexOf("/ont")-2,ind+1);
                        s=s.replace(cr,"");//font 태그 제거 작업

                    }

                    if(s.contains("<br>"))//줄 띄움 태그 역시 삭제합니다.
                    {
                        s=s.replace("<br>","");
                    }
                    if(s.contains("SYNC"))//SYNC 태그에서 프레임 번호를 가져오는 작업입니다.
                    {
                        m=m+1;
                        Subtitle temp = new Subtitle();
                        arrayList.add(temp);
                        int st = 0;
                        int endp = 0;

                        for(int i=s.indexOf("SYNC");i<s.length();i++)
                        {
                            if(s.charAt(i) == '=')
                                st=i+1;
                            else if(s.charAt(i)== '>')
                            {
                                endp=i;
                                break;
                            }

                        }
                        String cr = s.substring(st,endp);
                        int time=Integer.valueOf(cr).intValue();
                        arrayList.get(m).frame = time;
                        String er = s.substring(s.indexOf("<"),s.indexOf(">")+1);
                        s=s.replace(er,"");

                    }
                    if(s.contains("P Class"))//P class 태그에서 자막의 type를 가져오는 작업입니다.
                    {
                        int st = 0;
                        int endp = 0;

                        for(int i=s.indexOf("P Class");i<s.length();i++)
                        {
                            if(s.charAt(i) == '=')
                                st=i+1;
                            else if(s.charAt(i)== '>')
                            {
                                endp=i;
                                break;
                            }

                        }
                        String cr = s.substring(st,endp);
                        arrayList.get(m).langCode=cr;
                        String er = s.substring(s.indexOf("<"),s.indexOf(">")+1);
                        s=s.replace(er,"");

                    }
                    //자막 내용들을 ARRAYLIST 성분에 넣는 작업입니다. 위의 선별 작업을 거치고나면 남은것을 자막내용으로 넣습니다.
                    if(s!="")
                    {

                        if(m>=0)
                        {
                            arrayList.get(m).sentence += s;

                        }

                    }

                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bReader != null) bReader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<arrayList.size();i++)
        {
            System.out.println(arrayList.get(i).frame);
            System.out.println(arrayList.get(i).langCode);
            System.out.println(arrayList.get(i).sentence);
            System.out.println("\r\n");
        }
        return arrayList;
    }

}
