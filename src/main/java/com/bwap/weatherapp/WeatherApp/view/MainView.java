package com.bwap.weatherapp.WeatherApp.view;

import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;


@SpringUI(path = "")
public class MainView<cityName> extends UI {
    @Autowired
    private WeatherService weatherService;
    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button searchButton;
    private Label location ;
    private Label currentTemp;
    private Label weatherDescription;
    private Label weatherMin;
    private Label weatherMax;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label feelsLike;
    private Image iconImg;
    private HorizontalLayout Dashboard;
    private HorizontalLayout mainDescriptionLayout;
    private Image logo;
    private HorizontalLayout footer;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setUpLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();

        footer();
        searchButton.addClickListener(clickEvent -> {
           if (!cityTextField.getValue().equals("")){
               try {
                   updateUI();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }else
               Notification.show("Please Enter The City");
        });


    }



    public void setUpLayout() {
        logo = new Image();
        iconImg = new Image();
        iconImg.setWidth("200px");
        iconImg.setHeight("200px");
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName("BackColorGrey");
        setContent(mainLayout);
    }
    private void setHeader(){
        HorizontalLayout headerlayout = new HorizontalLayout();
        headerlayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label Title = new Label("Weather APP By Saad Aslam");
        Title.addStyleName(ValoTheme.LABEL_H1);
        Title.addStyleName(ValoTheme.LABEL_BOLD);
        Title.addStyleName(ValoTheme.LABEL_COLORED);

        headerlayout.addComponents(Title);
        mainLayout.addComponents(headerlayout);


    }
    private void setLogo() {
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

        logo.setSource(new ExternalResource("https://lh3.googleusercontent.com/proxy/xsHMx9xGm0BQmcoPVHjyn4U3dLcmPVX_5VazKGvIBpPmMyumkvx-YQEfy7UV17J139vRUHePYQ9hIsy-BgVLq8BV1T3WMD-4bqYbQCMNPdoeg4QGPU4"));
        logo.setWidth("500px");
        logo.setHeight("340px");
        logo.setVisible(true);
        
        logoLayout.addComponents(logo);
        mainLayout.addComponents(logoLayout);
    }
    private void setForm(){
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        //Selection Components
        unitSelect = new NativeSelect<>();
        unitSelect.setWidth("70px");
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponents(unitSelect);


        //cityTextField
        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponents(cityTextField);

        //Search Icon
        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(searchButton);


        mainLayout.addComponents(formLayout);
    }
    private void dashboardTitle() {
        Dashboard = new HorizontalLayout();
        Dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        //City Location
        location = new Label("Currently in Bhakkar");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        //Current Temp
        currentTemp = new Label("10F");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);
        Dashboard.addComponents(location,iconImg, currentTemp);


    }
    private void dashboardDetails(){
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //descriptjon Layout
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Weather Description  dummyData
        weatherDescription = new Label("Description: Clear Skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        descriptionLayout.addComponents(weatherDescription);

        //Min weather   dummyData
        weatherMin = new Label("Min:53");
        descriptionLayout.addComponents(weatherMin);
        //Max weather dummyData
        weatherMax = new Label("Max:22");
        descriptionLayout.addComponents(weatherMax);

        // Pressure, humidity, wind, Felslike dummyData

        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressureLabel = new Label("Pressure:123Pa");
        pressureLayout.addComponents(pressureLabel);

        humidityLabel = new Label("Humidity:34");
        pressureLayout.addComponents(humidityLabel);

        windSpeedLabel = new Label("124/hr");
        pressureLayout.addComponents(windSpeedLabel);

        feelsLike = new Label("FeelsLike:");
        pressureLayout.addComponents(feelsLike);




        mainDescriptionLayout.addComponents(descriptionLayout, pressureLayout);

    }

    //footer

    private void footer(){
        footer = new HorizontalLayout();
        footer.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        footer.setSpacing(true);
        footer.setMargin(true);
        footer.setWidth("100%");
        footer.setHeight("40px");
        Label description = new Label();
        description.setValue("Weather App by Saad Aslam . Visit my Github Repo for complete code of this project.https://github.com/saadusufzai/WeatherApp ");
        footer.addComponents(description);
        mainLayout.addComponents(footer);
    }


    //UI Update Method
    //Will be invoked only when Search Button is Clicked
    private void updateUI() throws JSONException {
        //Getting City name from text field and assignig it to API URL
        String city = cityTextField.getValue();
        String defaultUnit;
        weatherService.setCityName(city);

        //Checking Units and Assigning value to API url using setUnit()
        if(unitSelect.getValue().equals("F")){
            weatherService.setUnit( "imperials");
            unitSelect.setValue("F");
            defaultUnit = "\u00b0"+"F";
            }else {
            weatherService.setUnit("metric");
            defaultUnit = "\u00b0" + "C";
            unitSelect.setValue("C");
        }


        //Updaing City Temp and Unit
        location.setValue("Currently in "+city);
        JSONObject mainObject = weatherService.returnMainObject();
        int temp = mainObject.getInt("temp");
        currentTemp.setValue(temp + defaultUnit);


        // getting Icon form API
        String iconCode = null;
        String weatherDescriptionNew = null;
        JSONArray jsonArray = weatherService.returnWeatherArray();
         for (int i = 0; i< jsonArray.length(); i++){
            JSONObject weatherObject = jsonArray.getJSONObject(i);
            iconCode = weatherObject.getString("icon");
            weatherDescriptionNew = weatherObject.getString("description");
            System.out.println(iconCode);
             }
        //Setting icon between city name and temp
         iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));
        //Setting Icon as Main Logo

        logo.setSource(new ExternalResource("http://openweathermap.org/img/wn/" + iconCode + "@2x.png"));


        //updating Weather Description
        weatherDescription.setValue("Description: "+weatherDescriptionNew);



        //Updating Max Temp
        weatherMax.setValue("Max Temp: "+weatherService.returnMainObject().getInt("temp_max")+"\u00b0" +unitSelect.getValue());
        //Updating Min Temp
        weatherMin.setValue("Min Temp: "+weatherService.returnMainObject().getInt("temp_min")+"\u00b0" +unitSelect.getValue());
        //Updating Pressure
        pressureLabel.setValue("Pressure: "+weatherService.returnMainObject().getInt("pressure"));
        //Updating Humidity
        humidityLabel.setValue("Humidity: "+weatherService.returnMainObject().getInt("humidity"));

        //Updating Wind
        windSpeedLabel.setValue("Wind: "+weatherService.returnWindObject().getInt("speed")+"m/s");
        //Updating Feels Like
        feelsLike.setValue("Feelslike: "+weatherService.returnMainObject().getDouble("feels_like"));






        //Adding Dashboard and main Description
        //it will only appear when the search button is clicked
        mainLayout.addComponents(Dashboard,mainDescriptionLayout,footer);
    }







}