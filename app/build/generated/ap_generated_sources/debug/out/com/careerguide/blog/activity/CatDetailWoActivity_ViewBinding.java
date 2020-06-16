// Generated code from Butter Knife. Do not modify!
package com.careerguide.blog.activity;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.careerguide.R;
import com.github.ybq.android.spinkit.SpinKitView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CatDetailWoActivity_ViewBinding implements Unbinder {
  private CatDetailWoActivity target;

  @UiThread
  public CatDetailWoActivity_ViewBinding(CatDetailWoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CatDetailWoActivity_ViewBinding(CatDetailWoActivity target, View source) {
    this.target = target;

    target.id_toolbar = Utils.findRequiredViewAsType(source, R.id.id_toolbar, "field 'id_toolbar'", Toolbar.class);
    target.id_rv = Utils.findRequiredViewAsType(source, R.id.id_rv, "field 'id_rv'", RecyclerView.class);
    target.id_sw = Utils.findRequiredViewAsType(source, R.id.id_sw, "field 'id_sw'", SwipeRefreshLayout.class);
    target.id_progress = Utils.findRequiredViewAsType(source, R.id.id_progress, "field 'id_progress'", SpinKitView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CatDetailWoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.id_toolbar = null;
    target.id_rv = null;
    target.id_sw = null;
    target.id_progress = null;
  }
}
