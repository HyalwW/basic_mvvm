package com.demo.basic;

import com.demo.basic.base.BaseFloatService;
import com.demo.basic.base.FloatPage;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public class PageManager {
    private static volatile PageManager manager;
    private HashMap<Class<? extends BaseFloatService>, Stack<FloatPage>> pages;

    private PageManager() {
        pages = new HashMap<>();
    }

    public static PageManager getManager() {
        synchronized (PageManager.class) {
            if (manager == null) {
                manager = new PageManager();
            }
        }
        return manager;
    }

    public void add(BaseFloatService service, FloatPage page) {
        if (pages.containsKey(service.getClass())) {
            Stack<FloatPage> floatPages = pages.get(service.getClass());
            floatPages.push(page);
        } else {
            Stack<FloatPage> floatPages = new Stack<>();
            floatPages.push(page);
            pages.put(service.getClass(), floatPages);
        }
    }

    public FloatPage pop(BaseFloatService service) {
        if (pages.containsKey(service.getClass())) {
            Stack<FloatPage> floatPages = pages.get(service.getClass());
            if (!floatPages.empty()) {
                return floatPages.pop();
            }
        }
        return null;
    }

    public void remove(BaseFloatService service, FloatPage page) {
        if (pages.containsKey(service.getClass())) {
            Stack<FloatPage> floatPages = pages.get(service.getClass());
            floatPages.remove(page);
        }
    }

    public int getPageSize(BaseFloatService service) {
        if (pages.containsKey(service.getClass())) {
            return pages.get(service.getClass()).size();
        }
        return 0;
    }
}
