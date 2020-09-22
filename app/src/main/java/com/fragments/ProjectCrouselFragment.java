package com.fragments;

/*
public class ProjectCrouselFragment extends Fragment {
	
	private static CrouselOnItemClickListner listner;
	
	
//	public static Fragment newInstance(ProjectMapActivity context, int pos, float scale, PropertyCaraouselVO vo)
//	{
//		Bundle b = new Bundle();
//		b.putInt("pos", pos);
//		b.putFloat("scale", scale);
//		b.putParcelable("propertyvo", vo);
//		listner = context;
//		return Fragment.instantiate(context, ProjectCrouselFragment.class.getName(), b);
//	}
	
//	public ProjectCrouselFragment(ProjectMapActivity c){
//		listner = c;
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		final LinearLayout l = (LinearLayout) inflater.inflate(R.layout.project_crousel_item, container, false);
		
		int pos = this.getArguments().getInt("pos");
		final PropertyCaraouselVO propVO = this.getArguments().getParcelable("propertyvo");
		
		LinearLayout llContent = (LinearLayout) l.findViewById(R.id.ll_crousel);
		llContent.setTag(pos);
		ImageView img = (ImageView) l.findViewById(R.id.imageViewProp);
		TextView tvTitle = (TextView) l.findViewById(R.id.textViewTitle);
		TextView tvAdd = (TextView) l.findViewById(R.id.textViewAdd);
		TextView tvBHK = (TextView) l.findViewById(R.id.textViewBHK);
		TextView tvPSF = (TextView) l.findViewById(R.id.textViewPSF);
		TextView tvMinPrice = (TextView) l.findViewById(R.id.textViewMinPrice);
		TextView tvRating = (TextView) l.findViewById(R.id.textViewRating);
		TextView tvBuilder = (TextView)l.findViewById(R.id.textViewBuilder);
		
		TextView tvInfra = (TextView)l.findViewById(R.id.textViewInfra);
		TextView tvNeed = (TextView)l.findViewById(R.id.textViewNeeds);
		TextView tvLifeStyle = (TextView)l.findViewById(R.id.textViewLifestyle);
		TextView tvConstraction = (TextView)l.findViewById(R.id.under_constraction);
		
		TextView tvreturns = (TextView)l.findViewById(R.id.returns);
		
		
		
		
		
		
		
//		TextView tvTest = (TextView)l.findViewById(R.id.test);
		
		HashMap<String, String> map = ((ProjectMapActivity)getActivity()).searchMap;//(.get("type").equalsIgnoreCase("e-Auction");
		if(map !=null && map.get("type").equalsIgnoreCase("E-Auction")){
			tvPSF.setText("-");
		}else{
			tvPSF.setText(propVO.getPsf()+ " Psf");
			
			//getResources().getString(R.string.Rs)+" "+
		}
		
		tvTitle.setText(propVO.getDisplay_name());
		tvAdd.setText(propVO.getAddress());
		
		tvBHK.setText(propVO.getUnit_type());
		tvRating.setText(propVO.getRatings_average());
		tvBuilder.setText(propVO.getBuilder());
		
		tvInfra.setText(propVO.getInfra());
		tvNeed.setText(propVO.getNeeds());
		tvLifeStyle.setText(propVO.getLife_style());
		
		tvreturns.setText( propVO.getPrice_trends());
		
//		getResources().getDrawable(R.drawable.returns_year) +
		
		tvConstraction.setText(  propVO.getUnder_construction());
		
//		tvTest.setText(propVO.getProject_test());
		
		float stPrice = 0;
		try {
			stPrice = Float.parseFloat(propVO.getPrice_min());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
//		tvStartPrice.setText(app.getDecimalFormatedPrice(stPrice));
		BMHApplication app = (BMHApplication) getActivity().getApplication();
		tvMinPrice.setText(app.getDecimalFormatedPrice(stPrice));
		
		img.setTag(pos);
		if(!propVO.getBanner_img().equalsIgnoreCase("")){
			String imgUrl = UrlFactory.getShortImageByWidthUrl(BMHConstants.CRAOUSEL_IMAGE_WIDTH, propVO.getBanner_img());
			System.out.println("hh short url ="+imgUrl);
			Picasso.with(getActivity())
			.load(imgUrl)
			.placeholder(BMHConstants.PLACE_HOLDER)
			.error(BMHConstants.NO_IMAGE)
			.resize(BMHConstants.CRAOUSEL_IMAGE_WIDTH, BMHConstants.CRAOUSEL_IMAGE_HEIGHT)
			.into(img);
		}
		
//		MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);
//		float scale = this.getArguments().getFloat("scale");
//		root.setScaleBoth(scale);
		
		llContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listner!= null) {
					System.out.println("hh seting pos ="+v.getTag());
					listner.onclickCrouselItem(v);
				}else{
					System.out.println("hh listner obj null");
				}
			}
		});
		
		//=============== OnClick Listener set on offer page section  
		
		Button buttonOffer = (Button) l.findViewById(R.id.btn_offer);
		buttonOffer.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				final Dialog dialog = new Dialog(getActivity(),	android.R.style.Theme_Translucent_NoTitleBar);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_offers);
//				 dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				 
//				 overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				
				  Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
		            Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);

//		            if(layout.getVisibility()==View.INVISIBLE){
//
//		                layout.startAnimation(slideUp);
//		                layout.setVisibility(View.VISIBLE);
//		        }
				
				

//				Window window = dialog.getWindow();
//				WindowManager.LayoutParams wlp = window.getAttributes();
//				wlp.gravity = Gravity.BOTTOM;
//				wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
//				window.setAttributes(wlp);
				dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,	LayoutParams.MATCH_PARENT);
				dialog.show();
				
				

				final Button exitButton = (Button) dialog.findViewById(R.id.offers);
				exitButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						getDialog().dismiss();
					}

					private Dialog getDialog() {
						// TODO Auto-generated method stub
						return dialog;
					}
				});
			}

			

			private void overridePendingTransition(int pushDownIn, int pushDownOut) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return l;
	}

}
*/
