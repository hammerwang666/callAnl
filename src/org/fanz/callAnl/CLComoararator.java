package org.fanz.callAnl;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Fanz
 * Date: 13-7-7
 * Time: обнГ11:07
 * To change this template use File | Settings | File Templates.
 */
public class CLComoararator implements Comparator {
    public int compare(Object obj1, Object obj2) {
        HashMap map1 = (HashMap) obj1;
        HashMap map2 = (HashMap) obj2;
       Integer faq1 = (Integer)map1.get("faq");
        Integer faq2 = (Integer)map2.get("faq");
        return faq2.compareTo(faq1);
    }
}
