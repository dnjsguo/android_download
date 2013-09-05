package com.appdear.client.model;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;


public class OyysShareApplicationInfo {
    
   public  CharSequence title;

   public Intent intent;

   public Drawable icon;

   public  boolean filtered;
   
   public boolean ischeck;
   
   public int id;

 public  void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OyysShareApplicationInfo)) {
            return false;
        }

        OyysShareApplicationInfo that = (OyysShareApplicationInfo) o;
        return title.equals(that.title) &&
                intent.getComponent().getClassName().equals(
                        that.intent.getComponent().getClassName());
    }

    public int hashCode() {
        int result;
        result = (title != null ? title.hashCode() : 0);
        final String name = intent.getComponent().getClassName();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
