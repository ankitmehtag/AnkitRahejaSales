package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.UrlFactory;
import com.model.BrokersRespDataModel;
import com.sp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AssignedLeadAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	ArrayList<BrokersRespDataModel.Data> brokerList;

	public AssignedLeadAdapter(Context context, ArrayList<BrokersRespDataModel.Data> brokerList) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.brokerList = brokerList;
	}

	@Override
	public int getCount() {
		if(brokerList == null)return 10;
		else return brokerList.size();
	}

	@Override
	public Object getItem(int position) {
		if(brokerList == null)return 0;
		return brokerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.my_transaction_row, null);
			/*holder = new ViewHolder();
			holder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
			holder.tv_broker_name = (TextView) convertView.findViewById(R.id.tv_project_name);
			holder.tv_broker_code = (TextView) convertView.findViewById(R.id.tv_dt_val);
			holder.tv_broker_status = (TextView) convertView.findViewById(R.id.tv_size_val);
			holder.iv_call = (ImageView) convertView.findViewById(R.id.iv_call);
			holder.iv_mail = (ImageView) convertView.findViewById(R.id.iv_mail);
			holder.iv_whatsapp = (ImageView) convertView.findViewById(R.id.iv_whatsapp);
			convertView.setTag(holder);*/
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*holder.tv_broker_name.setText(brokerList.get(position).getBroker_name());
		holder.tv_broker_code.setText("Broker Code: "+brokerList.get(position).getBroker_code());
		String status = "";
		if(brokerList.get(position).getStatus() == BrokersRespDataModel.ACTIVE) status = "Active";
		if(brokerList.get(position).getStatus() == BrokersRespDataModel.INACTIVE) status = "Inactive";
		if(brokerList.get(position).getStatus() == BrokersRespDataModel.SUSPENDED) status = "Suspended";
		if(status.isEmpty()){
			holder.tv_broker_status.setVisibility(View.GONE);
		} else{
			holder.tv_broker_status.setVisibility(View.VISIBLE);
			holder.tv_broker_status.setText(status + "("+ brokerList.get(position).getEmp_type() + ")");
		}
		imageUpdate(context,holder.iv_user_img,brokerList.get(position).getImg_url());
		holder.iv_call.setTag(brokerList.get(position).getContact_no());
		holder.iv_mail.setTag(brokerList.get(position).getEmail_id());
		holder.iv_whatsapp.setTag(brokerList.get(position).getWhatsapp_no());

		convertView.setTag(R.integer.broker_data, brokerList.get(position));
		holder.iv_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getTag() instanceof String && v.getTag() != null) {
					String mobileNo = (String) v.getTag();
					makeCall(context, mobileNo);
				}else{
					showToast("Contact number not available");
				}
			}
		});
		holder.iv_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getTag() instanceof String && v.getTag() != null) {
					String email = (String) v.getTag();
					openMailClient(context, "", new String[]{email}, "");
				}else{
					showToast("Email id not available");
				}
			}
		});
		holder.iv_whatsapp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getTag() instanceof String && v.getTag() != null) {
					String mobileNo = (String) v.getTag();
					openWhatsApp(context, mobileNo);
				}else{
					showToast("Whatsapp number not available");
				}
			}
		});
*/
		return convertView;

	}
	class ViewHolder {
		ImageView iv_user_img,iv_call,iv_mail,iv_whatsapp;
		TextView tv_broker_name,tv_broker_code,tv_broker_status;
	}

	private void imageUpdate(Context ctx,ImageView iv,String imgPath){
		if(ctx != null && iv != null && imgPath != null && !imgPath.isEmpty()){
			String imgUrl = UrlFactory.IMG_BASEURL + imgPath;
			Picasso.Builder builder = new Picasso.Builder(ctx);
			builder.listener(new Picasso.Listener() {
				@Override
				public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
					exception.printStackTrace();
					//TODO:
				}
			});
			builder.build().load(imgUrl).placeholder(R.drawable.ic_profile_circle).error(R.drawable.ic_profile_circle).into(iv,new com.squareup.picasso.Callback() {
				@Override
				public void onSuccess() {
					//TODO:
				}
				@Override
				public void onError() {
					//TODO:
				}
			});
		}
	}


	private void openMailClient(Context ctx,String subject,String[] to, String bodyText){
		if(ctx == null)return;
		try {
			Intent mailer = new Intent(Intent.ACTION_SEND);
			//mailer.setType("text/plain");
			mailer.setType("message/rfc822");
			mailer.putExtra(Intent.EXTRA_EMAIL, to);
			mailer.putExtra(Intent.EXTRA_SUBJECT, subject);
			mailer.putExtra(Intent.EXTRA_TEXT, bodyText);
			ctx.startActivity(Intent.createChooser(mailer, "Send email..."));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


/*	private void makeCall(Context ctx,String mobileNo){
		if(ctx == null)return;
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		String contactNo = "tel:" + mobileNo;
		callIntent.setData(Uri.parse(contactNo));
		ctx.startActivity(callIntent);
	}*/

	private void openWhatsApp(Context ctx,String whatsappNo) {
		try {
			String toNumber = whatsappNo.replace("+", "").replace(" ", "");
			Intent sendIntent = new Intent("android.intent.action.MAIN");
			sendIntent.putExtra("Naresh", toNumber + "@s.whatsapp.net");
			sendIntent.putExtra(Intent.EXTRA_TEXT, "");
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.setPackage("com.whatsapp");
			sendIntent.setType("text/plain");
			ctx.startActivity(sendIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showToast(String message){
		if(context == null)return;
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}



}
