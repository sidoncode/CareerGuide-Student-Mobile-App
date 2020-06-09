// Generated code from Butter Knife. Do not modify!
package com.careerguide.blog.activity;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.careerguide.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CategoryActivity_ViewBinding implements Unbinder {
  private CategoryActivity target;

  @UiThread
  public CategoryActivity_ViewBinding(CategoryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CategoryActivity_ViewBinding(CategoryActivity target, View source) {
    this.target = target;

    target.id_toolbar = Utils.findRequiredViewAsType(source, R.id.id_toolbar, "field 'id_toolbar'", Toolbar.class);
    target.id_rv = Utils.findRequiredViewAsType(source, R.id.id_rv, "field 'id_rv'", RecyclerView.class);
    target.Cat_Blog = Utils.findRequiredViewAsType(source, R.id.Cat_Blog, "field 'Cat_Blog'", TextView.class);
    target.related_rv = Utils.findRequiredViewAsType(source, R.id.related_rv, "field 'related_rv'", RecyclerView.class);
    target.shimmer_view_container = Utils.findRequiredViewAsType(source, R.id.shimmer_view_container, "field 'shimmer_view_container'", ShimmerFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CategoryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.id_toolbar = null;
    target.id_rv = null;
    target.Cat_Blog = null;
    target.related_rv = null;
    target.shimmer_view_container = null;
  }
}
