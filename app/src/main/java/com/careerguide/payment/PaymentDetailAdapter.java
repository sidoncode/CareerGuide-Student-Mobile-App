package com.careerguide.payment;

        import android.app.Activity;
        import android.content.Context;
        import android.content.SharedPreferences;
        import androidx.recyclerview.widget.RecyclerView;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.careerguide.R;
        import java.util.List;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.PaymentItemViewHolder> {
    private Context context;
    private List<PaymentDetailModel> paymentList;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class PaymentItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_all_topic_item_container;
        TextView name, payment_email , amount, orderIdValue , status , paidon , validTill;

        public PaymentItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            payment_email = itemView.findViewById(R.id.payment_email);
            amount = itemView.findViewById(R.id.amount);
            orderIdValue = itemView.findViewById(R.id.orderIdValue);
            status = itemView.findViewById(R.id.status);
            paidon = itemView.findViewById(R.id.paidon);
            validTill = itemView.findViewById(R.id.validTill);

        }
    }

    public PaymentDetailAdapter(Context context, List<PaymentDetailModel> paymentItemList) {
        this.context = context;
        this.paymentList = paymentItemList;
    }

    @Override
    public PaymentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_check_out, parent, false);
        return new PaymentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PaymentItemViewHolder holder, int position) {
        final PaymentDetailModel paymentmodel = paymentList.get(position);
        holder.name.setText(paymentmodel.getName());
        holder.payment_email.setText(paymentmodel.getemail());
        holder.amount.setText(paymentmodel.getAmount());
        holder.orderIdValue.setText(paymentmodel.getOrder_id());
        holder.status.setText(paymentmodel.getPayment_status());
        holder.paidon.setText(paymentmodel.getDatetime());
        holder.validTill.setText(paymentmodel.getValidate_till());
        Log.e("#insdie adapter ","--> ");
//        holder.tv_name.setText(paymentmodel.getName());
//        holder.tv_courses.setText(paymentmodel.getCount()+" Classes");
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

}
