package com.appdear.client.utility;

import android.app.Activity;

/**
 * 作用：防止软件在低版本机器运行出现：VerifyError错误！
 * @author zxy
 *
 */
public class ActivityAnimationModel {   
    private Activity context;
    
    public ActivityAnimationModel(Activity context){   
        this.context = context;   
    }
    
    /**  
     * call overridePendingTransition() on the supplied Activity.  
     * @param a   
     * @param b  
     */   
    public void overridePendingTransition(int a, int b){   
        context.overridePendingTransition(a, b);   
    }   
}  