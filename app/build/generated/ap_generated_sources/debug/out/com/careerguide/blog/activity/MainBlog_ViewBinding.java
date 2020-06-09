// Generated code from Butter Knife. Do not modify!
package com.careerguide.blog.activity;

import android.view.View;
import android.widget.Toolbar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.careerguide.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainBlog_ViewBinding implements Unbinder {
  private MainBlog target;

  private View view7f0800b5;

  private View view7f0800b8;

  private View view7f0800b9;

  @UiThread
  public MainBlog_ViewBinding(MainBlog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainBlog_ViewBinding(final MainBlog target, View source) {
    this.target = target;

    View view;
    target.id_toolbar = Utils.findRequiredViewAsType(source, R.id.id_toolbar, "field 'id_toolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.btn_category, "method 'open_category'");
    view7f0800b5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.open_category();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_post, "method 'open_post'");
    view7f0800b8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.open_post();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_post_without, "method 'open_post_without'");
    view7f0800b9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.open_post_without();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainBlog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.id_toolbar = null;

    view7f0800b5.setOnClickListener(null);
    view7f0800b5 = null;
    view7f0800b8.setOnClickListener(null);
    view7f0800b8 = null;
    view7f0800b9.setOnClickListener(null);
    view7f0800b9 = null;
  }
}
