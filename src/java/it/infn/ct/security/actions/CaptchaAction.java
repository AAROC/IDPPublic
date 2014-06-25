/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class CaptchaAction extends ActionSupport {
        private static String res="";
        private static int rightId;

        private String imgs="";
        private ArrayList<String> items=new ArrayList<String>();
        
        public CaptchaAction(){
                Random rd= new Random();
                int id=0, rand=rd.nextInt(5);
                String tmp;
                Collections.addAll(items, "pencil" , "scissors" , "clock" , "heart" , "note");
                Collections.shuffle(items);
                Iterator<String> it = items.iterator();
                for (int i=0; i<5 && it.hasNext(); i++) {
                    id=rd.nextInt(Integer.MAX_VALUE);
                    tmp=it.next();
                    if(i==rand){
                        rightId=id;
                        res=tmp;
                    }
                    imgs+="$(\".ajax-fc-"+i+"\").html(\"<img src=\\\"captcha/imgs/item-"+tmp+
                            ".png\\\" alt=\\\"\\\" id=\\\""+id+"\\\" />\");\n";
                    imgs+="$(\".ajax-fc-"+i+"\").addClass(\'ajax-fc-highlighted\');\n";
                    imgs+="$(\".ajax-fc-"+i+"\").draggable({ containment: \'#ajax-fc-content\'});\n";
                }
        }
        
    public static int getRightId() {
            return rightId;
    }

    public static void setRightId(int rightId) {
        CaptchaAction.rightId = rightId;
    }
        
    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getRes() {
        return res;
    }

    public static void setRes(String res) {
        CaptchaAction.res = res;
    }

    @Override
    public String execute() {
            return SUCCESS;
    }

    public String display() {
            return NONE;
    }
}
