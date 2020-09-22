package com.sp;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PriceTrendsActivity extends Activity {

	LinearLayout llGraph; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price_trends);
		llGraph = (LinearLayout) findViewById(R.id.ll_graph);
		drawChart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.price_trends, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void drawChart() {

		// Creating an XYSeries for Income
		XYSeries happynessSeries = new XYSeries("Min");
		// Creating an XYSeries for Income
		XYSeries energySeries = new XYSeries("Average");
		// Adding data to Income and Expense Series
		XYSeries strenthSeries = new XYSeries("Max");

		ArrayList<Integer> aar = new ArrayList<Integer>();
		aar.add(1);
		aar.add(2);
		aar.add(3);
		aar.add(4);
		aar.add(5);
		aar.add(4);
		aar.add(3);
		aar.add(2);
		aar.add(1);
		aar.add(1);
		
		ArrayList<Integer> aar1 = new ArrayList<Integer>();
		aar1.add(2);
		aar1.add(3);
		aar1.add(4);
		aar1.add(5);
		aar1.add(6);
		aar1.add(5);
		aar1.add(4);
		aar1.add(3);
		aar1.add(2);
		aar1.add(1);
		
		ArrayList<Integer> aar2 = new ArrayList<Integer>();
		aar2.add(3);
		aar2.add(4);
		aar2.add(5);
		aar2.add(6);
		aar2.add(7);
		aar2.add(8);
		aar2.add(7);
		aar2.add(6);
		aar2.add(5);
		aar2.add(4);
		
		for (int i = 0; i < 10; i++) {
			happynessSeries.add(i, aar.get(i));
			energySeries.add(i, aar1.get(i));
			strenthSeries.add(i, aar2.get(i));
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(happynessSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(energySeries);
		dataset.addSeries(strenthSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer happynessRenderer = new XYSeriesRenderer();
		happynessRenderer.setColor(Color.parseColor("#ff0000"));// chart border
																// color
		happynessRenderer.setPointStyle(PointStyle.POINT);
		happynessRenderer.setFillPoints(false);
		happynessRenderer.setLineWidth(5);
		happynessRenderer.setDisplayChartValues(false);
		happynessRenderer.setFillBelowLine(false);
		happynessRenderer.setFillBelowLineColor(Color.parseColor("#ff0000"));

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer energyRenderer = new XYSeriesRenderer();
		energyRenderer.setColor(Color.parseColor("#00ff00"));
		energyRenderer.setPointStyle(PointStyle.POINT);
		energyRenderer.setFillPoints(false);
		energyRenderer.setLineWidth(5);
		energyRenderer.setDisplayChartValues(false);
		energyRenderer.setFillBelowLine(false);
		energyRenderer.setFillBelowLineColor(Color.parseColor("#00ff00"));

		// strenth
		XYSeriesRenderer strenthRenderer = new XYSeriesRenderer();
		strenthRenderer.setColor(Color.parseColor("#0000ff"));
		strenthRenderer.setPointStyle(PointStyle.POINT);
		strenthRenderer.setFillPoints(false);
		strenthRenderer.setLineWidth(5);
		strenthRenderer.setDisplayChartValues(false);
		strenthRenderer.setFillBelowLine(false);
		strenthRenderer.setFillBelowLineColor(Color.parseColor("#0000ff"));

//		// endurance
//		XYSeriesRenderer enduranceRenderer = new XYSeriesRenderer();
//		enduranceRenderer.setColor(Color.parseColor(enduranceColorCode));
//		enduranceRenderer.setPointStyle(PointStyle.POINT);
//		enduranceRenderer.setFillPoints(false);
//		enduranceRenderer.setLineWidth(5);
//		enduranceRenderer.setDisplayChartValues(false);
//		enduranceRenderer.setFillBelowLine(false);
//		enduranceRenderer.setFillBelowLineColor(Color.parseColor(enduranceColorCode));

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		// multiRenderer.setChartTitle("");
		// multiRenderer.setXTitle("");
		// multiRenderer.setYTitle("");
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		multiRenderer.setXLabels(0);
		multiRenderer.setZoomButtonsVisible(true);
		
		for (int i = 0; i < aar.size(); i++) {
			multiRenderer.addXTextLabel(i, aar.get(i)+"");

		}
		multiRenderer.setLabelsTextSize(30);
		multiRenderer.setYLabels(0);
		multiRenderer.setMargins(new int[] { 100, 100, 100, 100 });
		// multiRenderer.clearXTextLabels();
		multiRenderer.setShowLegend(false);
		multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
		// multiRenderer.clearYTextLabels();
		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(happynessRenderer);
		multiRenderer.addSeriesRenderer(energyRenderer);
		multiRenderer.addSeriesRenderer(strenthRenderer);

		multiRenderer.setPanEnabled(false, false);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setInScroll(true);
		// Creating an intent to plot line chart using dataset and
		// multipleRenderer
		GraphicalView mChart = (GraphicalView) ChartFactory.getLineChartView(this, dataset,multiRenderer);
//		int height = (int) getResources().getDimension(R.dimen.dashboard_graph_height);
		mChart.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mChart.setBackgroundColor(Color.TRANSPARENT);

		llGraph.addView(mChart);

	}
}
