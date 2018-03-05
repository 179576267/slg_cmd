/**
O * Copyright (c) 2013 DuoKu Inc.
 */

package com.wzf.yjdemo.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
import com.wzf.yjdemo.R;
import com.wzf.yjdemo.utils.LoginHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**ChargeView
 */
public class ChargeView extends BaseView implements OnClickListener {

	private ImageView mChangeAccountView;
	private ImageView mRecharge;
	private ImageView mAccountPic;
	private ImageView mPay100;
	private ImageView mRcharge100;
	private ImageView mPay;
	private ImageView mExtendPay;
	private ImageView mRole;

	private AlertDialog dlg;
	
	private Activity getActivity(){
		return (Activity)mContext;
	}
	
	public ChargeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	@Override
	protected void onFinishInflate() {
		initView(mContext);
	}
	public void initView(Context context) {

		mChangeAccountView = (ImageView) findViewById(R.id.sf_changeaccount);
		mChangeAccountView.setOnClickListener(this);
		mRecharge = (ImageView) findViewById(R.id.sf_recharge);

		mRecharge.setOnClickListener(this);
		mPay = (ImageView) findViewById(R.id.sf_pay);
		mPay.setOnClickListener(this);

		mAccountPic = (ImageView) findViewById(R.id.sf_account_pic);
		mAccountPic.setOnClickListener(this);

		mPay100 = (ImageView) findViewById(R.id.sf_pay100);
		mPay100.setOnClickListener(this);

		mRcharge100 = (ImageView) findViewById(R.id.sf_recharge100);
		mRcharge100.setOnClickListener(this);
		
		mExtendPay = (ImageView) findViewById(R.id.sf_extendpay);
		mExtendPay.setOnClickListener(this);
		
		mRole = (ImageView) findViewById(R.id.sf_role);
		mRole.setOnClickListener(this);
	}


	

	@Override
	public void onClick(View v) {
        //充值
		if(mRecharge == v) { 
			showRechargeAlert();
		} else if(mPay==v){
			showPayDialog();
		} else if(mPay100 == v){
			pay(100);
		} else if(mRcharge100 == v){
			recharge(100);
		} else if(mChangeAccountView == v){
			Logout();
		} else if(mExtendPay == v){
			showExendPayDialog();
		}else if(mRole==v){
			showRoleDialog();
		}
	}
	private void showRoleDialog() {
		

		LayoutInflater inflater = LayoutInflater.from(ChargeView.this.getActivity());
		final View DialogView = inflater.inflate(R.layout.sf_role_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity()); 
		builder.setView(DialogView);
		dlg = builder.create();
		dlg.show();
		final EditText setDataKey = (EditText)DialogView.findViewById(R.id.sf_set_data_key);
		Button btConfirm1 = (Button)DialogView.findViewById(R.id.sf_confirm);
		Button btCancel1 = (Button)DialogView.findViewById(R.id.sf_cancel);
		btConfirm1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataKey = setDataKey.getText().toString();
				try {
					JSONObject roleInfo = new JSONObject();
					roleInfo.put("roleId", "1");         //当前登录的玩家角色ID，必须为数字
					roleInfo.put("roleName", "猎人");  //当前登录的玩家角色名，不能为空，不能为null
					roleInfo.put("roleLevel", "100");   //当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
					roleInfo.put("zoneId", "1");       //当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
					roleInfo.put("zoneName", "阿狸一区");//当前登录的游戏区服名称，不能为空，不能为null
					roleInfo.put("balance", "0");  //用户游戏币余额，必须为数字，若无，传入0
					roleInfo.put("vip", "1");            //当前用户VIP等级，必须为数字，若无，传入1
					roleInfo.put("partyName", "无帮派");//当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
					roleInfo.put("roleCTime", "21322222");	 //单位为秒，创建角色的时间
					roleInfo.put("roleLevelMTime", "54456556");	//单位为秒，角色等级变化时间
					SFOnlineHelper.setData(ChargeView.this.getActivity(), dataKey, roleInfo.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
				dlg.dismiss();
			}
		});
		btCancel1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
	
	}

	private void recharge(int price) {
		if (!LoginHelper.instance().isLogin()) {
			Toast.makeText(ChargeView.this.getActivity(), "用户未登陆",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("ganga", "Demo recharge！！！");
		SFOnlineHelper.charge(ChargeView.this.getActivity(),  "金币",price, 1,
				"购买金币", LoginHelper.CP_PAY_SYNC_URL, new SFOnlinePayResultListener() {
					@Override
					public void onSuccess(String remain) {
					}
					@Override
					public void onFailed(String remain) {
					}
					@Override
					public void onOderNo(String orderNo) {
						LoginHelper.showMessage("订单号:" + orderNo, ChargeView.this.getActivity());
					}
				});
	}
	private void showPayDialog() {
		LayoutInflater inflater = LayoutInflater.from(ChargeView.this.getActivity());
		final View DialogView = inflater.inflate(R.layout.paydialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity()); 
		builder.setView(DialogView);
		dlg = builder.create();
		dlg.show();
		final EditText num = (EditText)DialogView.findViewById(R.id.sf_paytype);
		final EditText name1 = (EditText)DialogView.findViewById(R.id.s_itemname);
		Button btConfirm1 = (Button)DialogView.findViewById(R.id.confirm);
		Button btCancel1 = (Button)DialogView.findViewById(R.id.cancel);
		btConfirm1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String chargeNum1 = num.getText().toString();
				String Itemname1= name1.getText().toString();
				if(chargeNum1.equals("")) {
					chargeNum1 = "0";
				}
				if(isNumeric(chargeNum1)) {
					if(chargeNum1.length() > 10)
						Toast.makeText(ChargeView.this.getActivity(), "请输入正确金额", Toast.LENGTH_LONG).show();
					else {
						dlg.dismiss();
						Pay(chargeNum1,Itemname1);
					}						
				}
				else 
					Toast.makeText(ChargeView.this.getActivity(), "请输入正确金额", Toast.LENGTH_LONG).show();
			}
		});
		btCancel1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
	}
	private void showExendPayDialog() {
		LayoutInflater inflater = LayoutInflater.from(ChargeView.this.getActivity());
		final View DialogView = inflater.inflate(R.layout.extend_pay_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity()); 
		builder.setView(DialogView);
		dlg = builder.create();
		dlg.show();
		final EditText price = (EditText)DialogView.findViewById(R.id.sf_paytype);
		final EditText name1 = (EditText)DialogView.findViewById(R.id.s_itemname);
		final EditText waresid = (EditText)DialogView.findViewById(R.id.s_yijie_extendpay_waresid);
		final EditText remain = (EditText)DialogView.findViewById(R.id.s_yijie_extendpay_remain);//s_yijie_extendpay_remain
		Button btConfirm1 = (Button)DialogView.findViewById(R.id.confirm);
		Button btCancel1 = (Button)DialogView.findViewById(R.id.cancel);
		btConfirm1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String chargeNum1 = price.getText().toString();
				String Itemname1= name1.getText().toString();
				String sdkWaresid = waresid.getText().toString();
				String remainStr = remain.getText().toString();
				if(chargeNum1.equals("")) {
					chargeNum1 = "0";
				}
				if(isNumeric(chargeNum1)) {
					if(chargeNum1.length() > 10)
						Toast.makeText(ChargeView.this.getActivity(), "请输入正确金额", Toast.LENGTH_LONG).show();
					else {
						dlg.dismiss();
						extendPay(chargeNum1,Itemname1,sdkWaresid, remainStr);
					}						
				}
				else 
					Toast.makeText(ChargeView.this.getActivity(), "请输入正确金额", Toast.LENGTH_LONG).show();
			}
		});
		btCancel1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
	}
	private void extendPay(String strAmount,String strName, String waresid, String remain) {
		if (!LoginHelper.instance().isLogin()) {
			Toast.makeText(ChargeView.this.getActivity(), "用户未登陆",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("ganga", "Demo Pay name = "+strName+" price = "+Integer.valueOf(strAmount));
		SFOnlineHelper.payExtend(ChargeView.this.getActivity(), Integer.valueOf(strAmount),
				strName, waresid, remain, 1,"", LoginHelper.CP_PAY_SYNC_URL, new SFOnlinePayResultListener() {
					@Override
					public void onSuccess(String remain) {
					}
					@Override
					public void onFailed(String remain) {
					}
					@Override
					public void onOderNo(String orderNo) {
						LoginHelper.showMessage("订单号:" + orderNo, ChargeView.this.getActivity());
					}
				});
	}
	private void Pay(String strAmount,String strName) {
		if (!LoginHelper.instance().isLogin()) {
			Toast.makeText(ChargeView.this.getActivity(), "用户未登陆",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("ganga", "Demo Pay name = "+strName+" price = "+Integer.valueOf(strAmount));
		SFOnlineHelper.pay(ChargeView.this.getActivity(),Integer.valueOf(strAmount), strName,  1,
				"购买金币", LoginHelper.CP_PAY_SYNC_URL, new SFOnlinePayResultListener() {
					@Override
					public void onSuccess(String remain) {
					}
					@Override
					public void onFailed(String remain) {
					}
					@Override
					public void onOderNo(String orderNo) {
						LoginHelper.showMessage("订单号:" + orderNo, ChargeView.this.getActivity());
					}
				});
	}
	private void Logout(){
		SFOnlineHelper.logout(this.getActivity(), "LoginOut");
		
	}
	private void pay(int price){
		if (!LoginHelper.instance().isLogin()) {
			Toast.makeText(ChargeView.this.getActivity(), "用户未登陆",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("ganga", "Demo pay！！！");

        /* pay定额计费接口
		 * public static void pay (Context context, int unitPrice, String unitName,
		 * int count, String callBackInfo, String callBackUrl,SFOnlinePayResultListener payResultListener)
		 * 
		 *  @param context       上下文Activity
		 *  @param unitPrice     当前商品需要支付金额，单位为人民币分
		 *  @param itemName      虚拟货币名称
		 *  @param count         用户选择购买道具界面的默认道具数量。（总价 count*unitPrice）
		 *  @param callBackInfo  由游戏开发者定义传入的字符串，会与支付结果一同发送给游戏服务器，游戏服务器可通过该字段判断交易的详细内容（金额角色等）
		 *  @param callBackUrl   将支付结果通知给游戏服务器时的通知地址url，交易结束后，系统会向该url发送http请求，通知交易的结果金额callbackInfo等信息
		 *  @param payResultListener  支付回调接口
		*/
		SFOnlineHelper.pay(ChargeView.this.getActivity(), price, "金币", 1,
				"购买金币", LoginHelper.CP_PAY_SYNC_URL, new SFOnlinePayResultListener() {

					@Override
					public void onSuccess(String remain) {
					}

					@Override
					public void onFailed(String remain) {
					}

					@Override
					public void onOderNo(String orderNo) {
						LoginHelper.showMessage("订单号:" + orderNo, ChargeView.this.getActivity());
					}
				});
	}
	private void recharge(String strAmount,String strName) {
		if (!LoginHelper.instance().isLogin()) {
			Toast.makeText(ChargeView.this.getActivity(), "用户未登陆",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("ganga", "Demo recharge name = "+strName+" price = "+Integer.valueOf(strAmount));
		/* charge非定额计费接口
		 * 1.	public static void charge(Context context, String itemName, int unitPrice,
		 *          int count,String callBackInfo, String callBackUrl,SFOnlinePayResultListener payResultListener)
		 * 
		 *  @param context       上下文Activity
		 *  @param itemName      虚拟货币名称
		 *  @param unitPrice     游戏道具单位价格，单位为人民币分
		 *  @param count         用户选择购买道具界面的默认道具数量。（总价 count*unitPrice）
		 *  @param callBackInfo  由游戏开发者定义传入的字符串，会与支付结果一同发送给游戏服务器，游戏服务器可通过该字段判断交易的详细内容（金额角色等）
		 *  @param callBackUrl   将支付结果通知给游戏服务器时的通知地址url，交易结束后，系统会向该url发送http请求，通知交易的结果金额callbackInfo等信息
		 *  @param payResultListener  支付回调接口
		*/
		SFOnlineHelper.charge(ChargeView.this.getActivity(), strName, Integer.valueOf(strAmount), 1,
				"购买金币", LoginHelper.CP_PAY_SYNC_URL, new SFOnlinePayResultListener() {
					@Override
					public void onSuccess(String remain) {
					}

					@Override
					public void onFailed(String remain) {
					}

					@Override
					public void onOderNo(String orderNo) {
						LoginHelper.showMessage("订单号:" + orderNo, ChargeView.this.getActivity());
					}
				});
	}
	
	private void showRechargeAlert() {

		LayoutInflater inflater = LayoutInflater.from(ChargeView.this.getActivity());
		final View DialogView = inflater.inflate(R.layout.chargedialog, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity()); 
		builder.setView(DialogView);
			
		dlg = builder.create();
		dlg.show();
		
		final EditText ed = (EditText)DialogView.findViewById(R.id.sf_chargetype);
		final EditText name = (EditText)DialogView.findViewById(R.id.sf_itemname);
		Button btConfirm = (Button)DialogView.findViewById(R.id.sf_confirm);
		Button btCancel = (Button)DialogView.findViewById(R.id.sf_cancel);
		btConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String chargeNum = ed.getText().toString();
				String Itemname= name.getText().toString();
				if(chargeNum.equals("")) {
					chargeNum = "0";
				}
				
				//单次最大支付金额为99999元
				if(isNumeric(chargeNum)) {
					if(chargeNum.length() >10)
						Toast.makeText(ChargeView.this.getActivity(), "请输入正确金额", Toast.LENGTH_LONG).show();
					else {
						dlg.dismiss();
						recharge(chargeNum,Itemname);
					}						
				}
				else 
					Toast.makeText(ChargeView.this.getActivity(), "请输入正确金额", Toast.LENGTH_LONG).show();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});

	}
	
	public boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}