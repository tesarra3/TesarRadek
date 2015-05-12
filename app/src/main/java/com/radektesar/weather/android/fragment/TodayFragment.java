package com.radektesar.weather.android.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.radektesar.weather.android.R;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.client.APICallListener;
import com.radektesar.weather.android.client.APICallManager;
import com.radektesar.weather.android.client.APICallTask;
import com.radektesar.weather.android.client.ResponseStatus;
import com.radektesar.weather.android.client.request.WeatherRequest;
import com.radektesar.weather.android.client.response.Response;
import com.radektesar.weather.android.database.DatabaseManager;
import com.radektesar.weather.android.database.model.Forecast;
import com.radektesar.weather.android.database.model.Store;

import com.radektesar.weather.android.entity.WorldWeatherOnlineResponse;
import com.radektesar.weather.android.utility.Logcat;
import com.radektesar.weather.android.utility.NetworkManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Radek on 18. 3. 2015.
 */
public class TodayFragment extends Fragment implements APICallListener
{

	@InjectView(R.id.TodayCityState)
	TextView todayCityState;
	@InjectView(R.id.TodayTemperatureCondition)
	TextView todayTemperatureCondition;
	@InjectView(R.id.TodayHumidity)
	TextView todayHumidity;
	@InjectView(R.id.TodayPrecipitation)
	TextView todayPrecipitation;
	@InjectView(R.id.TodayPresure)
	TextView todayPresure;
	@InjectView(R.id.TodayWindSpeed)
	TextView todayWindSpeed;
	@InjectView(R.id.TodayDiction)
	TextView todayDiction;
	@InjectView(R.id.TodayIcon)
	ImageView todayIcon;


	int mStoreSize;
	int mMilemeter;
	int mDeegres;
	private View rootView;
	Store mStore = new Store();
	Forecast mForecast = new Forecast();
	private WorldWeatherOnlineResponse mJsonUsers = null;
	private DatabaseManager mDatabaseManager;
	private SharedPreferences mPrefs;
	protected APICallManager mAPICallManager = new APICallManager();
	private final Object mLock = new Object();
	private Boolean mReady = false;

	private List<Runnable> mPendingCallbacks = new LinkedList<Runnable>();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{


		rootView = inflater.inflate(R.layout.fragment_today, container, false);
		ButterKnife.inject(TodayFragment.this, rootView);

		mDatabaseManager = new DatabaseManager(getActivity().getBaseContext());
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());


		if(NetworkManager.isOnline(getActivity()))
		{
			if(!mAPICallManager.hasRunningTask(WeatherRequest.class))
			{
				// showProgress();

				WeatherRequest request = new WeatherRequest();
				mAPICallManager.executeTask(request, this);

			}
		}
		else
		{
			// showOffline();
		}


		//Call method for set views
		setView();


		return rootView;


	}


	public void setView()
	{


		ButterKnife.inject(getActivity());

		mDatabaseManager = new DatabaseManager(RadkeTesarApplication.getAppContext());
		mPrefs = PreferenceManager.getDefaultSharedPreferences(RadkeTesarApplication.getAppContext());

		//Check preference
		mMilemeter = Integer.parseInt(mPrefs.getString("lenghtsUnits", "0"));
		mDeegres = Integer.parseInt(mPrefs.getString("tempritureUnits", "0"));
		try
		{


			//Fill view from last rec in database
			mStoreSize = mDatabaseManager.getStores().size() - 1;
			if(mDatabaseManager.getStores().size()!=0)
			{


				todayCityState.setText(mDatabaseManager.getStores().get(mStoreSize).getCityCountry());
				if(mDeegres!=0)
				{
					todayTemperatureCondition.setText(mDatabaseManager.getStores().get(mStoreSize).getTempritureF());
				}
				else
				{
					todayTemperatureCondition.setText(mDatabaseManager.getStores().get(mStoreSize).getTempriture());

				}
				todayHumidity.setText(mDatabaseManager.getStores().get(mStoreSize).getHumidity());
				todayPrecipitation.setText(mDatabaseManager.getStores().get(mStoreSize).getPrecipitation());
				todayPresure.setText(mDatabaseManager.getStores().get(mStoreSize).getPresure());
				if(mMilemeter!=0)
				{

					todayWindSpeed.setText(mDatabaseManager.getStores().get(mStoreSize).getWindSpeedM());

				}
				else
				{
					todayWindSpeed.setText(mDatabaseManager.getStores().get(mStoreSize).getWindSpeed());
				}

				todayDiction.setText(mDatabaseManager.getStores().get(mStoreSize).getWinddirection());

				Picasso.with(getActivity().getBaseContext()).load(mDatabaseManager.getStores().get(mStoreSize).getUrlToday()).into(todayIcon);


			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}


	}


	@Override
	public void onAPICallRespond(final APICallTask task, final ResponseStatus status, final Response<?> response)
	{
		runTaskCallback(new Runnable() {
			public void run() {
				if (rootView == null) return; // view was destroyed

				if (task.getRequest().getClass().equals(WorldWeatherOnlineResponse.class)) {

					handleKidListSuccessResponse((Response<WorldWeatherOnlineResponse>) response, task, status);


				}

				// finish request
				mAPICallManager.finishTask(task);
			}
		});





	}


	private void handleKidListSuccessResponse(Response<WorldWeatherOnlineResponse> response, APICallTask task, ResponseStatus status)
	{

		mJsonUsers = response.getResponseObject();
		int foracastDatabaseSize = 0;

		try
		{
			foracastDatabaseSize = mDatabaseManager.getForecasts().size();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		// saving data for today into the database
		mStore.setCityCountry(mJsonUsers.getData().getRequest().get(0).getQuery());
		mStore.setTempriture(mJsonUsers.getData().getCurrentCondition().get(0).getTempC() + "°C | " + mJsonUsers.getData().getCurrentCondition().get(0).getWeatherDesc().get(0).getValue());
		mStore.setTempritureF(mJsonUsers.getData().getCurrentCondition().get(0).getTempF() + "°F | " + mJsonUsers.getData().getCurrentCondition().get(0).getWeatherDesc().get(0).getValue());
		mStore.setHumidity(mJsonUsers.getData().getCurrentCondition().get(0).getHumidity() + "%");
		mStore.setPrecipitation(mJsonUsers.getData().getCurrentCondition().get(0).getPrecipMM() + " mm");
		mStore.setPresure(mJsonUsers.getData().getCurrentCondition().get(0).getPressure() + " hPa");
		mStore.setWindSpeed(mJsonUsers.getData().getCurrentCondition().get(0).getWindspeedKmph() + " Km/h");
		mStore.setWindSpeedM(mJsonUsers.getData().getCurrentCondition().get(0).getWindspeedKmph() + " m/h");
		mStore.setWinddirection(mJsonUsers.getData().getCurrentCondition().get(0).getWinddir16Point());
		mStore.setUrlToday(mJsonUsers.getData().getCurrentCondition().get(0).getWeatherIconUrl().get(0).getValue());

		//checking if is something in database model Forecast
		// if yes it gonna save data
		//if not it gonna refresh them
		if(foracastDatabaseSize<=2)
		{


			for(int i = 0; i<=4; i++)
			{
				mForecast.setForecastDayOfWeek(checkDay(i));
				mForecast.setForecastTempritureC(mJsonUsers.getData().getWeather().get(i).getMaxtempC() + "°C");
				mForecast.setForecastTempritureF(mJsonUsers.getData().getWeather().get(i).getMaxtempF() + "°F");
				mForecast.setForecastDescription(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherDesc().get(0).getValue());
				mForecast.setForecastUrl(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherIconUrl().get(0).getValue());

				try
				{
					mDatabaseManager.addForecast(mForecast);
				}
				catch(java.sql.SQLException e)
				{
					e.printStackTrace();
				}

			}
		}
		else
		{
			for(int i = 0; i<=4; i++)
			{
				mForecast.setForecastDayOfWeek(checkDay(i));
				mForecast.setForecastTempritureC(mJsonUsers.getData().getWeather().get(i).getMaxtempC() + "°C");
				mForecast.setForecastDescription(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherDesc().get(0).getValue());
				mForecast.setForecastTempritureF(mJsonUsers.getData().getWeather().get(i).getMaxtempF() + "°F");
				mForecast.setForecastUrl(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherIconUrl().get(0).getValue());

				try
				{
					mDatabaseManager.updateForecast(mForecast);
				}
				catch(java.sql.SQLException e)
				{
					e.printStackTrace();
				}

			}


		}


		try
		{
			mDatabaseManager.addStore(mStore);
		}
		catch(java.sql.SQLException e)
		{
			e.printStackTrace();
		}
		setView();
	}


	@Override
	public void onAPICallFail(APICallTask task, ResponseStatus status, Exception exception)
	{
		Logcat.d("Server doesn§t respond");
	}


	public String checkDay(int i)
	{
		String mSomething = "";
		Calendar cal = Calendar.getInstance();

		int today = cal.get(Calendar.DAY_OF_WEEK);


		int day = today + i;

		if(day>=8)
		{
			day = day - 7;
		}

		switch(day)
		{
			case 2:
			{
				mSomething = "Monday";
				return mSomething;
			}
			case 3:
			{
				mSomething = "Tuesday";
				return mSomething;
			}
			case 4:
			{
				mSomething = "Wednesday";
				return mSomething;
			}
			case 5:
			{
				mSomething = "Thursday";
				return mSomething;
			}
			case 6:
			{
				mSomething = "Friday";
				return mSomething;
			}
			case 7:
			{
				mSomething = "Suturday";
				return mSomething;
			}
			case 1:
			{
				mSomething = "Sunday";
				return mSomething;
			}
			default:
			{

				return mSomething;
			}
		}


	}

	public void runTaskCallback(Runnable runnable)
	{
		if (mReady) runNow(runnable);
		else addPending(runnable);
	}

	private void runNow(Runnable runnable)
	{
		//Logcat.d("TaskFragment.runNow(): " + runnable.getClass().getEnclosingMethod());
		getActivity().runOnUiThread(runnable);
	}
	private void addPending(Runnable runnable)
	{
		synchronized (mLock)
		{
			//Logcat.d("TaskFragment.addPending(): " + runnable.getClass().getEnclosingMethod());
			mPendingCallbacks.add(runnable);
		}
	}
	//Set up views when fragment is open
	@Override
	public void onResume()
	{
		super.onResume();
		refreshList();
	}


	// update
	private void refreshList()
	{
		setView();


	}
}
