package com.bob.bobapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.AddressTypeAdapter;
import com.bob.bobapp.adapters.CityAdapter;
import com.bob.bobapp.adapters.CountryAdapter;
import com.bob.bobapp.adapters.GenderAdapter;
import com.bob.bobapp.adapters.GrossIncomeAdapter;
import com.bob.bobapp.adapters.MaritalStatusAdapter;
import com.bob.bobapp.adapters.NationalityAdapter;
import com.bob.bobapp.adapters.OccupationAdapter;
import com.bob.bobapp.adapters.PoliticalExposedAdapter;
import com.bob.bobapp.adapters.StateAdapter;
import com.bob.bobapp.adapters.WealthSourceAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.CallClientCreationActivationRequest;
import com.bob.bobapp.api.request_object.CallClientCreationActivationRequestBody;
import com.bob.bobapp.api.request_object.FinacleClientDetailsRequest;
import com.bob.bobapp.api.request_object.FinacleClientDetailsRequestBody;
import com.bob.bobapp.api.request_object.GetDropDownDatasForKYCRegisteredRequest;
import com.bob.bobapp.api.response_object.AddressType;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.CallClientCreationActivationResponse;
import com.bob.bobapp.api.response_object.City;
import com.bob.bobapp.api.response_object.Country;
import com.bob.bobapp.api.response_object.FinacleClientDetailsResponse;
import com.bob.bobapp.api.response_object.Gender;
import com.bob.bobapp.api.response_object.GetDropDownDatasForKYCRegisteredResponse;
import com.bob.bobapp.api.response_object.GrossAnnIncome;
import com.bob.bobapp.api.response_object.MaritalStatus;
import com.bob.bobapp.api.response_object.NationalitiesResponse;
import com.bob.bobapp.api.response_object.Occupation;
import com.bob.bobapp.api.response_object.PoliticalFigure;
import com.bob.bobapp.api.response_object.SourceOFWealth;
import com.bob.bobapp.api.response_object.State;
import com.bob.bobapp.utility.BMSPrefs;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewWealthMgmtActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtCancel, txtNext;
    private LinearLayout linearNominee, linearGuardianName, linearGuardianName2, linearGuardianName3;
    private AppCompatEditText edtPanNumber, edtName, edtDateOfBirth, edtFatherOrHusbandName, edtMotherMaidenName,
            edtEmail, edtMobile, edtAddress1, edtAddress2, edtAddress3, edtPin, edtBirthPalce, edtINR,
            edtNomineeName, edtNomineeDob, edtRelationship, edtNomineeShare, edtNomineeAddress,
            edtGurdianName, edtNominee2Name, edtNominee2Dob, edtRelationship2, edtNominee2Share, edtNominee2Address,
            edtGurdianName2, edtNominee3Name, edtNominee3Dob, edtRelationship3, edtNominee3Share, edtNominee3Address,
            edtGurdianName3;
    private String panNumber, name, dob, fatherOrHusbandName, motherName, email, mobileNumber, address1, address2,
            address3, pin, birthPlace, inr, nomineeName, nomineeDob, nomineRelationship, nomineeShare = "",
            nomineeAddress, guardianName, guardian2Name, guardian3Name;
    private String nationality = "", gender = "", maritalStatus = "", addressType = "", city = "", state = "",
            country = "", occupation = "", politicalExposed = "", grossAnnualIncome = "", wealthSource = "",
            birthCountry = "", isMinor = "false", isNominee = "false", nominee2Name = "", nominee2Dob = "", nominee2Relationship = "",
            nominee2Share = "", nominee2Address = "", isMinor2 = "false", nominee2Status = "false", nominee3Name = "", nominee3Dob = "", nominee3Relationship = "",
            nominee3Share = "", nominee3Address = "", isMinor3 = "false", nominee3Status = "false";
    private AppCompatSpinner spinnerGender, spinnerMaritalStatus, spineerCity, spineerState, spineerCountry,
            spineerAddressType, spineerOccuptaion, spineerPoliticalExposed, spineerGrossIncome,
            spineerWealthSource, spineerNationality, spineerBirthCountry;
    private RadioButton radioMinorYes, radioMinorNo, radioNimineeYes, radioNimineeNo,
            radioMinor2Yes, radioMinor2No, radioMinor3Yes, radioMinor3No;
    private APIInterface apiInterface;
    private Util util;
    private GenderAdapter genderAdapter;
    private MaritalStatusAdapter maritalStatusAdapter;
    private CityAdapter cityAdapter;
    private StateAdapter stateAdapter;
    private CountryAdapter countryAdapter;
    private AddressTypeAdapter addressTypeAdapter;
    private OccupationAdapter occupationAdapter;
    private PoliticalExposedAdapter politicalExposedAdapter;
    private GrossIncomeAdapter grossIncomeAdapter;
    private WealthSourceAdapter wealthSourceAdapter;
    private NationalityAdapter nationalityAdapter;
    private ArrayList<Gender> genderArrayList = new ArrayList<>();
    private ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();
    private ArrayList<City> cityArrayList = new ArrayList<>();
    private ArrayList<State> stateArrayList = new ArrayList<>();
    private ArrayList<Country> countryArrayList = new ArrayList<>();
    private ArrayList<AddressType> addressTypeArrayList = new ArrayList<>();
    private ArrayList<Occupation> occupationArrayList = new ArrayList<>();
    private ArrayList<PoliticalFigure> politicalFigureArrayList = new ArrayList<>();
    private ArrayList<GrossAnnIncome> grossAnnIncomeArrayList = new ArrayList<>();
    private ArrayList<SourceOFWealth> sourceOFWealthArrayList = new ArrayList<>();
    private ArrayList<NationalitiesResponse> nationalitiesResponseArrayList = new ArrayList<>();
    private Calendar calendar;
    int year, month, dayOfMonth;
    private DatePickerDialog datePickerDialog;
    private String ucc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wealth_mgmt);
        findView();
    }

    private void findView() {
        ucc = BMSPrefs.getString(getApplicationContext(), "ucc");
        //    Toast.makeText(getApplicationContext(), ucc, Toast.LENGTH_SHORT).show();
        txtCancel = findViewById(R.id.txtCancel);
        linearNominee = findViewById(R.id.linearNominee);
        linearGuardianName = findViewById(R.id.linearGuardianName);
        txtNext = findViewById(R.id.txtNext);
        edtPanNumber = findViewById(R.id.edtPanNumber);
        edtName = findViewById(R.id.edtName);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
        edtFatherOrHusbandName = findViewById(R.id.edtFatherOrHusbandName);
        edtMotherMaidenName = findViewById(R.id.edtMotherMaidenName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        edtAddress1 = findViewById(R.id.edtAddress1);
        edtAddress2 = findViewById(R.id.edtAddress2);
        edtAddress3 = findViewById(R.id.edtAddress3);
        edtPin = findViewById(R.id.edtPin);
        edtBirthPalce = findViewById(R.id.edtBirthPalce);
        edtINR = findViewById(R.id.edtINR);
        edtNomineeName = findViewById(R.id.edtNomineeName);
        edtNomineeDob = findViewById(R.id.edtNomineeDob);
        edtRelationship = findViewById(R.id.edtRelationship);
        edtNomineeShare = findViewById(R.id.edtNomineeShare);
        edtNomineeAddress = findViewById(R.id.edtNomineeAddress);
        edtGurdianName = findViewById(R.id.edtGurdianName);
        radioMinorYes = findViewById(R.id.radioMinorYes);
        radioMinorNo = findViewById(R.id.radioMinorNo);
        radioNimineeYes = findViewById(R.id.radioNimineeYes);
        radioNimineeNo = findViewById(R.id.radioNimineeNo);

        edtNominee2Name = findViewById(R.id.edtNominee2Name);
        edtNominee2Dob = findViewById(R.id.edtNominee2Dob);
        edtRelationship2 = findViewById(R.id.edtRelationship2);
        edtNominee2Share = findViewById(R.id.edtNominee2Share);
        edtNominee2Address = findViewById(R.id.edtNominee2Address);
        radioMinor2Yes = findViewById(R.id.radioMinor2Yes);
        radioMinor2No = findViewById(R.id.radioMinor2No);
        linearGuardianName2 = findViewById(R.id.linearGuardianName2);
        edtGurdianName2 = findViewById(R.id.edtGurdianName2);

        edtNominee3Name = findViewById(R.id.edtNominee3Name);
        edtNominee3Dob = findViewById(R.id.edtNominee3Dob);
        edtRelationship3 = findViewById(R.id.edtRelationship3);
        edtNominee3Share = findViewById(R.id.edtNominee3Share);
        edtNominee3Address = findViewById(R.id.edtNominee3Address);
        radioMinor3Yes = findViewById(R.id.radioMinor3Yes);
        radioMinor3No = findViewById(R.id.radioMinor3No);
        linearGuardianName3 = findViewById(R.id.linearGuardianName3);
        edtGurdianName3 = findViewById(R.id.edtGurdianName3);


        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerMaritalStatus = findViewById(R.id.spinnerMaritalStatus);
        spineerCity = findViewById(R.id.spineerCity);
        spineerState = findViewById(R.id.spineerState);
        spineerCountry = findViewById(R.id.spineerCountry);
        spineerAddressType = findViewById(R.id.spineerAddressType);
        spineerOccuptaion = findViewById(R.id.spineerOccuptaion);
        spineerPoliticalExposed = findViewById(R.id.spineerPoliticalExposed);
        spineerGrossIncome = findViewById(R.id.spineerGrossIncome);
        spineerWealthSource = findViewById(R.id.spineerWealthSource);
        spineerNationality = findViewById(R.id.spineerNationality);
        spineerBirthCountry = findViewById(R.id.spineerBirthCountry);

        radioMinorYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMinor = "true";
                    radioMinorYes.setChecked(true);
                    radioMinorNo.setChecked(false);
                    linearGuardianName.setVisibility(View.VISIBLE);
                }
            }
        });

        radioMinorNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMinor = "false";
                    radioMinorYes.setChecked(false);
                    radioMinorNo.setChecked(true);
                    linearGuardianName.setVisibility(View.GONE);
                }
            }
        });

        radioMinor2No.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMinor2 = "false";
                    radioMinor2Yes.setChecked(false);
                    radioMinor2No.setChecked(true);
                    linearGuardianName2.setVisibility(View.GONE);
                }
            }
        });

        radioMinor2Yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMinor2 = "true";
                    radioMinor2Yes.setChecked(true);
                    radioMinor2No.setChecked(false);
                    linearGuardianName2.setVisibility(View.VISIBLE);
                }
            }
        });

        radioMinor3No.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMinor3 = "false";
                    radioMinor3Yes.setChecked(false);
                    radioMinor3No.setChecked(true);
                    linearGuardianName3.setVisibility(View.GONE);
                }
            }
        });

        radioMinor3Yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMinor3 = "true";
                    radioMinor3Yes.setChecked(true);
                    radioMinor3No.setChecked(false);
                    linearGuardianName3.setVisibility(View.VISIBLE);
                }
            }
        });

        radioNimineeYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isNominee = "true";
                    radioNimineeYes.setChecked(true);
                    radioNimineeNo.setChecked(false);
                    linearNominee.setVisibility(View.VISIBLE);
                }
            }
        });
        radioNimineeNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isNominee = "false";
                    radioNimineeYes.setChecked(false);
                    radioNimineeNo.setChecked(true);
                    linearNominee.setVisibility(View.GONE);
                }
            }
        });

        txtCancel.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        edtDateOfBirth.setOnClickListener(this);
        edtNomineeDob.setOnClickListener(this);
        edtNominee2Dob.setOnClickListener(this);
        edtNominee3Dob.setOnClickListener(this);

        //   BOBActivity.title.setText("Wealth Management");

        apiInterface = BOBApp.getApi(getApplicationContext(), Constants.ACTION_GET_DROPDOWN);
        // getDropDownApiCall();

        GetFinacleClientDetailsApi();

    }

    // calendar
    private void onDateCalendar() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(NewWealthMgmtActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        edtDateOfBirth.setText((month + 1) + "/" + day + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    // calendar
    private void onDateCalendar1() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(NewWealthMgmtActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        edtNomineeDob.setText((month + 1) + "/" + day + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    // calendar
    private void onDateCalendar2() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(NewWealthMgmtActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        edtNominee2Dob.setText((month + 1) + "/" + day + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }    // calendar

    private void onDateCalendar3() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(NewWealthMgmtActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        edtNominee3Dob.setText((month + 1) + "/" + day + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    // gender adapter
    private void setGenderAdapter() {
        Gender genders = new Gender();
        genders.setName("Select");
        genders.setValue("");
        genderArrayList.add(0, genders);

        genderAdapter = new GenderAdapter(getApplicationContext(), genderArrayList);
        spinnerGender.setAdapter(genderAdapter);
        for (int i = 0; i < genderArrayList.size(); i++) {
            if (genderArrayList.get(i).getName().equalsIgnoreCase(gender)) {
                spinnerGender.setSelection(i);
            }
        }
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spinnerGender.getSelectedItemPosition();
                gender = genderArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // marital status adapter
    private void setMaritalStatusAdapter()
    {
        MaritalStatus maritalStatuss = new MaritalStatus();
        maritalStatuss.setName("Select");
        maritalStatuss.setValue("0");
        maritalStatusArrayList.add(0, maritalStatuss);

        maritalStatusAdapter = new MaritalStatusAdapter(getApplicationContext(), maritalStatusArrayList);
        spinnerMaritalStatus.setAdapter(maritalStatusAdapter);

        for (int i = 0; i < maritalStatusArrayList.size(); i++) {
            if (maritalStatusArrayList.get(i).getName().equalsIgnoreCase(maritalStatus)) {
                spinnerMaritalStatus.setSelection(i);
            }
        }

        spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spinnerMaritalStatus.getSelectedItemPosition();
                maritalStatus = maritalStatusArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // city  adapter
    private void setCityAdapter() {
        City citys = new City();
        citys.setName("Select");
        citys.setValue("0");
        cityArrayList.add(0, citys);

        cityAdapter = new CityAdapter(getApplicationContext(), cityArrayList);
        spineerCity.setAdapter(cityAdapter);
        for (int i = 0; i < cityArrayList.size(); i++) {
            if (cityArrayList.get(i).getName().equalsIgnoreCase(city)) {
                spineerCity.setSelection(i);
            }
        }
        spineerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerCity.getSelectedItemPosition();
                //   city = cityArrayList.get(pos).getName();
                city = cityArrayList.get(pos).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    // state  adapter
    private void setStateAdapter() {
        State states = new State();
        states.setName("Select");
        states.setValue("0");
        stateArrayList.add(0, states);

        stateAdapter = new StateAdapter(getApplicationContext(), stateArrayList);
        spineerState.setAdapter(stateAdapter);
        for (int i = 0; i < stateArrayList.size(); i++) {
            if (stateArrayList.get(i).getName().equalsIgnoreCase(state)) {
                spineerState.setSelection(i);
            }
        }
        spineerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerState.getSelectedItemPosition();
                // state = stateArrayList.get(pos).getName();
                state = stateArrayList.get(pos).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // country  adapter
    private void setCountryAdapter() {
        Country countrys = new Country();

        countrys.setName("Select");
        countrys.setValue("0");
        countryArrayList.add(0, countrys);

        countryAdapter = new CountryAdapter(getApplicationContext(), countryArrayList);
        spineerCountry.setAdapter(countryAdapter);
        for (int i = 0; i < countryArrayList.size(); i++) {
            if (countryArrayList.get(i).getName().equalsIgnoreCase(country)) {
                spineerCountry.setSelection(i);
            }
        }
        spineerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerCountry.getSelectedItemPosition();
                // country = countryArrayList.get(pos).getName();
                country = countryArrayList.get(pos).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // birth country  adapter
    private void setBirthCountryAdapter() {
        countryAdapter = new CountryAdapter(getApplicationContext(), countryArrayList);
        spineerBirthCountry.setAdapter(countryAdapter);
        for (int i = 0; i < countryArrayList.size(); i++) {
            if (countryArrayList.get(i).getName().equalsIgnoreCase(birthCountry)) {
                spineerBirthCountry.setSelection(i);
            }
        }
        spineerBirthCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerBirthCountry.getSelectedItemPosition();
                birthCountry = countryArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // address type  adapter
    private void seAddressTypeAdapter() {

        AddressType addressTypes = new AddressType();

        addressTypes.setName("Select");
        addressTypes.setValue("0");

        addressTypeArrayList.set(0, addressTypes);


        addressTypeAdapter = new AddressTypeAdapter(getApplicationContext(), addressTypeArrayList);
        spineerAddressType.setAdapter(addressTypeAdapter);

        for (int i = 0; i < addressTypeArrayList.size(); i++) {
            if (addressTypeArrayList.get(i).getName().equalsIgnoreCase(addressType)) {
                spineerAddressType.setSelection(i);
            }
        }
        spineerAddressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerAddressType.getSelectedItemPosition();
                //   addressType = addressTypeArrayList.get(pos).getName();
                addressType = addressTypeArrayList.get(pos).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // occupation  adapter
    private void setOccupationAdapter() {
        Occupation occupation1 = new Occupation();
        occupation1.setName("Select");
        occupation1.setValue("0");
        occupationArrayList.add(0, occupation1);


        occupationAdapter = new OccupationAdapter(getApplicationContext(), occupationArrayList);
        spineerOccuptaion.setAdapter(occupationAdapter);

        for (int i = 0; i < occupationArrayList.size(); i++) {
            if (occupationArrayList.get(i).getName().equalsIgnoreCase(occupation)) {
                spineerOccuptaion.setSelection(i);
            }
        }
        spineerOccuptaion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerOccuptaion.getSelectedItemPosition();
                occupation = occupationArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    // political exposed  adapter
    private void setPoliticalExposedAdapter() {
        PoliticalFigure politicalFigure = new PoliticalFigure();
        politicalFigure.setName("Select");
        politicalFigure.setValue("0");
        politicalFigureArrayList.add(0, politicalFigure);


        politicalExposedAdapter = new PoliticalExposedAdapter(getApplicationContext(), politicalFigureArrayList);
        spineerPoliticalExposed.setAdapter(politicalExposedAdapter);

        for (int i = 0; i < politicalFigureArrayList.size(); i++) {
            if (politicalFigureArrayList.get(i).getName().equalsIgnoreCase(politicalExposed)) {
                spineerPoliticalExposed.setSelection(i);
            }
        }
        spineerPoliticalExposed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerPoliticalExposed.getSelectedItemPosition();
                politicalExposed = politicalFigureArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // gross income  adapter
    private void setGrossIncomeAdapter() {
        GrossAnnIncome grossAnnIncomes = new GrossAnnIncome();
        grossAnnIncomes.setName("Select");
        grossAnnIncomes.setValue("0");
        grossAnnIncomeArrayList.add(0, grossAnnIncomes);


        grossIncomeAdapter = new GrossIncomeAdapter(getApplicationContext(), grossAnnIncomeArrayList);
        spineerGrossIncome.setAdapter(grossIncomeAdapter);

        for (int i = 0; i < grossAnnIncomeArrayList.size(); i++) {
            if (grossAnnIncomeArrayList.get(i).getName().equalsIgnoreCase(grossAnnualIncome)) {
                spineerGrossIncome.setSelection(i);
            }
        }
        spineerGrossIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerGrossIncome.getSelectedItemPosition();
                grossAnnualIncome = grossAnnIncomeArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // wealth source adapter
    private void setWealthSourceAdapter() {
        SourceOFWealth sourceOFWealth = new SourceOFWealth();
        sourceOFWealth.setName("Select");
        sourceOFWealth.setValue("0");

        sourceOFWealthArrayList.add(0, sourceOFWealth);


        wealthSourceAdapter = new WealthSourceAdapter(getApplicationContext(), sourceOFWealthArrayList);
        spineerWealthSource.setAdapter(wealthSourceAdapter);

        for (int i = 0; i < sourceOFWealthArrayList.size(); i++) {
            if (sourceOFWealthArrayList.get(i).getName().equalsIgnoreCase(wealthSource)) {
                spineerWealthSource.setSelection(i);
            }
        }
        spineerWealthSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerWealthSource.getSelectedItemPosition();
                wealthSource = sourceOFWealthArrayList.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // nationality adapter
    private void setNationalityAdapter() {
        nationalityAdapter = new NationalityAdapter(getApplicationContext(), nationalitiesResponseArrayList);
        spineerNationality.setAdapter(nationalityAdapter);
        for (int i = 0; i < nationalitiesResponseArrayList.size(); i++) {
            if (nationalitiesResponseArrayList.get(i).getCountryName().equalsIgnoreCase(nationality)) {
                spineerNationality.setSelection(i);
            }
        }

        spineerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos = spineerNationality.getSelectedItemPosition();
                nationality = nationalitiesResponseArrayList.get(pos).getCountryName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtCancel) {
            onBackPressed();
        }
        else if (id == R.id.txtNext) {
            String result = validationForm();
            if (result.equalsIgnoreCase("success")) {
                CallClientCreationActivationApi();
            } else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.edtDateOfBirth) {
            // onDateCalendar();
        }

        else if (id == R.id.edtNomineeDob) {
            onDateCalendar1();
        }
        else if (id == R.id.edtNominee2Dob) {
            onDateCalendar2();
        } else if (id == R.id.edtNominee3Dob) {
            onDateCalendar3();
        }
        if (id == R.id.imgBack) {
            onBackPressed();
        }
    }

    // apply validation here...
    private String validationForm() {
        String result = "";
        panNumber = edtPanNumber.getText().toString().trim();
        name = edtName.getText().toString().trim();
        dob = edtDateOfBirth.getText().toString().trim();
        fatherOrHusbandName = edtFatherOrHusbandName.getText().toString().trim();
        motherName = edtMotherMaidenName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        mobileNumber = edtMobile.getText().toString().trim();
        address1 = edtAddress1.getText().toString().trim();
        address2 = edtAddress2.getText().toString().trim();
        address3 = edtAddress3.getText().toString().trim();
        pin = edtPin.getText().toString().trim();
        birthPlace = edtBirthPalce.getText().toString().trim();
        inr = edtINR.getText().toString().trim();
        nomineeName = edtNomineeName.getText().toString().trim();
        nomineeDob = edtNomineeDob.getText().toString().trim();
        nomineRelationship = edtRelationship.getText().toString().trim();
        nomineeShare = edtNomineeShare.getText().toString().trim();
        nomineeAddress = edtNomineeAddress.getText().toString().trim();
        guardianName = edtGurdianName.getText().toString().trim();

        nominee2Name = edtNominee2Name.getText().toString().trim();
        nominee2Dob = edtNominee2Dob.getText().toString().trim();
        nominee2Relationship = edtRelationship2.getText().toString().trim();
        nominee2Share = edtNominee2Share.getText().toString().trim();
        nominee2Address = edtNominee2Address.getText().toString().trim();
        guardian2Name = edtGurdianName2.getText().toString().trim();

        nominee3Name = edtNominee3Name.getText().toString().trim();
        nominee3Dob = edtNominee3Dob.getText().toString().trim();
        nominee3Relationship = edtRelationship3.getText().toString().trim();
        nominee3Share = edtNominee3Share.getText().toString().trim();
        nominee3Address = edtNominee3Address.getText().toString().trim();
        guardian3Name = edtGurdianName3.getText().toString().trim();


        if (TextUtils.isEmpty(panNumber)) {
            edtPanNumber.setFocusable(true);
            edtPanNumber.requestFocus();
            return getString(R.string.enter_pan_no);
        }
        if (TextUtils.isEmpty(name)) {
            edtName.setFocusable(true);
            edtName.requestFocus();
            return getString(R.string.enter_name);
        }
        if (TextUtils.isEmpty(dob)) {
            edtDateOfBirth.setFocusable(true);
            edtDateOfBirth.requestFocus();
            return getString(R.string.enter_dob);
        }
        if (TextUtils.isEmpty(fatherOrHusbandName)) {
            edtFatherOrHusbandName.setFocusable(true);
            edtFatherOrHusbandName.requestFocus();
            return getString(R.string.enter_husband);
        }

        if (TextUtils.isEmpty(motherName)) {
            edtMotherMaidenName.setFocusable(true);
            edtMotherMaidenName.requestFocus();
            return getString(R.string.enter_mother);
        }

        if (nationality.equalsIgnoreCase("select")) {
            return "please select nationality";
        }
        if (gender.equalsIgnoreCase("select")) {
            return "please select gender";
        }

        if (maritalStatus.equalsIgnoreCase("select")) {
            return "please select marital status";
        }

        if (addressType.equalsIgnoreCase("select")) {
            return "please select address type";
        }
        if (city.equalsIgnoreCase("select")) {
            return "please select city";
        }
        if (state.equalsIgnoreCase("select")) {
            return "please select state";
        }
        if (country.equalsIgnoreCase("select")) {
            return "please select country";
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setFocusable(true);
            edtEmail.requestFocus();
            return getString(R.string.enter_email);
        }
        if (TextUtils.isEmpty(mobileNumber)) {
            edtMobile.setFocusable(true);
            edtMobile.requestFocus();
            return getString(R.string.enter_mobile);
        }
        if (TextUtils.isEmpty(address1)) {
            edtAddress1.setFocusable(true);
            edtAddress1.requestFocus();
            return getString(R.string.enter_address);
        }
//        if (TextUtils.isEmpty(address2)) {
//            edtAddress2.setFocusable(true);
//            edtAddress2.requestFocus();
//            return getString(R.string.enter_address);
//        }
//        if (TextUtils.isEmpty(address3)) {
//            edtAddress3.setFocusable(true);
//            edtAddress3.requestFocus();
//            return getString(R.string.enter_address);
//        }

        if (TextUtils.isEmpty(pin)) {
            edtPin.setFocusable(true);
            edtPin.requestFocus();
            return getString(R.string.enter_pin_code);
        }
        if (TextUtils.isEmpty(birthPlace)) {
            edtBirthPalce.setFocusable(true);
            edtBirthPalce.requestFocus();
            return getString(R.string.enter_birth_place);
        }

        if (birthCountry.equalsIgnoreCase("select")) {
            return "please select birth country";
        }

        if (occupation.equalsIgnoreCase("select")) {
            return "please select occupation";
        }
        if (politicalExposed.equalsIgnoreCase("select")) {
            return "please select politically exposed";
        }

        if (grossAnnualIncome.equalsIgnoreCase("select")) {
            return "please select gross income";
        }
        if (wealthSource.equalsIgnoreCase("select")) {
            return "please select wealth Source";
        }


        if (TextUtils.isEmpty(inr)) {
            edtINR.setFocusable(true);
            edtINR.requestFocus();
            return getString(R.string.enter_inr);
        }

        if (isNominee.equalsIgnoreCase("true") && TextUtils.isEmpty(nomineeName)) {
            edtNomineeName.setFocusable(true);
            edtNomineeName.requestFocus();
            return "please enter nominee name";
        }
        if (isNominee.equalsIgnoreCase("true") && TextUtils.isEmpty(nomineeDob)) {
            edtNomineeDob.setFocusable(true);
            edtNomineeDob.requestFocus();
            return "please enter nominee dob";
        }

        if (isNominee.equalsIgnoreCase("true") && TextUtils.isEmpty(nomineRelationship)) {
            edtRelationship.setFocusable(true);
            edtRelationship.requestFocus();
            return "please enter nominee relationship";
        }
        if (isNominee.equalsIgnoreCase("true") && TextUtils.isEmpty(nomineeShare)) {
            edtNomineeShare.setFocusable(true);
            edtNomineeShare.requestFocus();
            return "please enter nominee share";
        }
        if (isNominee.equalsIgnoreCase("true") && TextUtils.isEmpty(nomineeAddress)) {
            edtNomineeAddress.setFocusable(true);
            edtNomineeAddress.requestFocus();
            return "please enter nominee address";
        }

        if (isMinor.equalsIgnoreCase("true") && TextUtils.isEmpty(guardianName)) {
            edtGurdianName.setFocusable(true);
            edtGurdianName.requestFocus();
            return "please enter guardian name";
        }


        if (TextUtils.isEmpty(nominee2Name) && TextUtils.isEmpty(nominee2Dob) && TextUtils.isEmpty(nominee2Relationship) && TextUtils.isEmpty(nominee2Share) && TextUtils.isEmpty(nominee2Address)) {
            nominee2Status = "true";
        } else {
            nominee2Status = "false";
            if (TextUtils.isEmpty(nominee2Name)) {
                edtNominee2Name.setFocusable(true);
                edtNominee2Name.requestFocus();
                return "please enter nominee2 name";
            }
            if (TextUtils.isEmpty(nominee2Dob)) {
                edtNominee2Dob.setFocusable(true);
                edtNominee2Dob.requestFocus();
                return "please enter nominee2 dob";
            }
            if (TextUtils.isEmpty(nominee2Relationship)) {
                edtRelationship2.setFocusable(true);
                edtRelationship2.requestFocus();
                return "please enter nominee2 relationship";
            }
            if (TextUtils.isEmpty(nominee2Share)) {
                edtNominee2Share.setFocusable(true);
                edtNominee2Share.requestFocus();
                return "please enter nominee2 share";
            }
            if (TextUtils.isEmpty(nominee2Address)) {
                edtNominee2Address.setFocusable(true);
                edtNominee2Address.requestFocus();
                return "please enter nominee2 address";
            }
        }

        if (isMinor2.equalsIgnoreCase("true") && TextUtils.isEmpty(guardian2Name)) {
            edtGurdianName2.setFocusable(true);
            edtGurdianName2.requestFocus();
            return "please enter nominee2 guardian name";
        }

        if (TextUtils.isEmpty(nominee3Name) && TextUtils.isEmpty(nominee3Dob) && TextUtils.isEmpty(nominee3Relationship) && TextUtils.isEmpty(nominee3Share) && TextUtils.isEmpty(nominee3Address)) {
            nominee3Status = "true";
        } else {
            nominee3Status = "false";
            if (TextUtils.isEmpty(nominee3Name)) {
                edtNominee3Name.setFocusable(true);
                edtNominee3Name.requestFocus();
                return "please enter nominee3 name";
            }
            if (TextUtils.isEmpty(nominee3Dob)) {
                edtNominee3Dob.setFocusable(true);
                edtNominee3Dob.requestFocus();
                return "please enter nominee3 dob";
            }
            if (TextUtils.isEmpty(nominee3Relationship)) {
                edtRelationship3.setFocusable(true);
                edtRelationship3.requestFocus();
                return "please enter nominee3 relationship";
            }
            if (TextUtils.isEmpty(nominee3Share)) {
                edtNominee3Share.setFocusable(true);
                edtNominee3Share.requestFocus();
                return "please enter nominee3 share";
            }
            if (TextUtils.isEmpty(nominee3Address)) {
                edtNominee3Address.setFocusable(true);
                edtNominee3Address.requestFocus();
                return "please enter nominee2 address";
            }
        }

        if (isMinor3.equalsIgnoreCase("true") && TextUtils.isEmpty(guardian3Name)) {
            edtGurdianName3.setFocusable(true);
            edtGurdianName3.requestFocus();
            return "please enter nominee3 guardian name";
        }

        double total = 0;

        double nomShare1, nomShare2, nomShare3;

        if (isNominee.equalsIgnoreCase("true")) {
            if (nomineeShare.equalsIgnoreCase("")) {
                nomShare1 = 0;
            } else {
                nomShare1 = Double.parseDouble(nomineeShare);
            }

            if (nominee2Share.equalsIgnoreCase("")) {
                nomShare2 = 0;
            } else {
                nomShare2 = Double.parseDouble(nominee2Share);
            }

            if (nominee3Share.equalsIgnoreCase("")) {
                nomShare3 = 0;
            } else {
                nomShare3 = Double.parseDouble(nominee3Share);
            }

            total = nomShare1 + nomShare2 + nomShare3;


            if (total > 100) {
                return "Total of Nominee share should not be greater than 100%";
            }
        }

        return result = "success";
    }


    // api call
    private void GetFinacleClientDetailsApi()
    {
        util.showProgressDialog(NewWealthMgmtActivity.this, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;


        FinacleClientDetailsRequestBody requestBody = new FinacleClientDetailsRequestBody();
        requestBody.setpInfinityID(ucc);

        // requestBody.setpInfinityID("BJF002853");

        FinacleClientDetailsRequest model = new FinacleClientDetailsRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getApplicationContext(), uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.GetFinacleClientDetails(model).enqueue(new Callback<FinacleClientDetailsResponse>() {
            @Override
            public void onResponse(Call<FinacleClientDetailsResponse> call, Response<FinacleClientDetailsResponse> response) {

                //    util.showProgressDialog(InvestmentCartActivity.this, false);

                if (response.isSuccessful()) {
                    edtPanNumber.setText(response.body().getPAN());
                    edtName.setText(response.body().getName());
                    edtDateOfBirth.setText(response.body().getBirthDt());
                    edtFatherOrHusbandName.setText(response.body().getFatherOrHusbandName());
                    edtMotherMaidenName.setText(response.body().getMotherName());
                    edtEmail.setText(response.body().getEmail());
                    edtMobile.setText(response.body().getMobNo());
                    edtAddress1.setText(response.body().getMAddrLine1());
                    edtAddress2.setText(response.body().getMAddrLine2());
                    edtAddress3.setText(response.body().getMAddrLine3());
                    edtPin.setText(response.body().getMPostalCode());
                    edtBirthPalce.setText(response.body().getBirthPlace());
                    edtINR.setText(response.body().getEstimatedFinancialGrowth());
                    nationality = response.body().getNationality();
                    gender = response.body().getGender();
                    maritalStatus = response.body().getMaritalStatus();
                    addressType = response.body().getAddressType();
                    city = response.body().getMCity();
                    state = response.body().getMState();
                    country = response.body().getMCountry();
                    occupation = response.body().getOccupationcode();
                    politicalExposed = response.body().getPoliticallyExposed();
                    grossAnnualIncome = response.body().getGrossannualincome();
                    wealthSource = response.body().getWealthSource();
                    birthCountry = response.body().getBirthCountry();
                    getDropDownApiCall();

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FinacleClientDetailsResponse> call, Throwable t) {
                util.showProgressDialog(getApplicationContext(), false);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // api calling
    private void getDropDownApiCall() {
        //   util.showProgressDialog(this, true);

        GetDropDownDatasForKYCRegisteredRequest model = new GetDropDownDatasForKYCRegisteredRequest();
        //  model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(NewWealthMgmtActivity.this, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.GetDropDownDatasForKYCRegistered(model).enqueue(new Callback<GetDropDownDatasForKYCRegisteredResponse>() {
            @Override
            public void onResponse(Call<GetDropDownDatasForKYCRegisteredResponse> call, Response<GetDropDownDatasForKYCRegisteredResponse> response) {

                //  util.showProgressDialog(WealthMgmtActivity.this, false);
                System.out.println("gendar: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    // gender
                    genderArrayList = response.body().getGender();
                    setGenderAdapter();

                    // marital status
                    maritalStatusArrayList = response.body().getMaritalStatus();
                    setMaritalStatusAdapter();

                    // city adapter;
                    cityArrayList = response.body().getCity();
                    setCityAdapter();

                    //state
                    stateArrayList = response.body().getState();
                    setStateAdapter();

                    // country
                    countryArrayList = response.body().getCountry();
                    setCountryAdapter();
                    setBirthCountryAdapter();

                    // address type
                    addressTypeArrayList = response.body().getAddressType();
                    seAddressTypeAdapter();

                    //occupation adapter
                    occupationArrayList = response.body().getOccupation();
                    setOccupationAdapter();

                    //political exposed
                    politicalFigureArrayList = response.body().getPoliticalFigure();
                    setPoliticalExposedAdapter();

                    // gross income
                    grossAnnIncomeArrayList = response.body().getGrossAnnIncome();
                    setGrossIncomeAdapter();

                    // wealth souce
                    sourceOFWealthArrayList = response.body().getSourceOFWealth();
                    setWealthSourceAdapter();

                    //api call
                    getNationalityDropDownApiCall();

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDropDownDatasForKYCRegisteredResponse> call, Throwable t) {
                util.showProgressDialog(getApplicationContext(), false);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // api calling
    private void getNationalityDropDownApiCall() {
        GetDropDownDatasForKYCRegisteredRequest model = new GetDropDownDatasForKYCRegisteredRequest();
        //  model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getApplicationContext(), uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getNationalities(model).enqueue(new Callback<ArrayList<NationalitiesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NationalitiesResponse>> call, Response<ArrayList<NationalitiesResponse>> response) {

                util.showProgressDialog(NewWealthMgmtActivity.this, false);

                if (response.isSuccessful()) {
                    nationalitiesResponseArrayList = response.body();
                    setNationalityAdapter();

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NationalitiesResponse>> call, Throwable t) {
                util.showProgressDialog(getApplicationContext(), false);
//                       Toast.makeText(WealthMgmtActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // CallClientCreationActivation api call
    private void CallClientCreationActivationApi() {

        util.showProgressDialog(NewWealthMgmtActivity.this, true);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;


        CallClientCreationActivationRequestBody requestBody = new CallClientCreationActivationRequestBody();

        //  requestBody.setpInfinityID(authenticateResponse.getClientUCC());
        requestBody.setpInfinityID(ucc);
        //   requestBody.setpInfinityID("BJF002853");
        requestBody.setConstitutionCode("");
        requestBody.setClientStatus("");
        requestBody.setCustStatus("");
        requestBody.setClientTaxId("");
        requestBody.setDateofIncorporation("");
        requestBody.setIsNRE("");
        requestBody.setPAN(panNumber);
        requestBody.setName(name);
        requestBody.setFirstName(name);
        requestBody.setMiddleName("");
        requestBody.setLastName("");
        requestBody.setBirthDt(dob);
        requestBody.setGender(gender);
        requestBody.setDefaultAddrType("");
        requestBody.setPassportNum("");
        requestBody.setMotherName(motherName);
        requestBody.setFatherOrHusbandName(fatherOrHusbandName);
        requestBody.setNationality(nationality);
        requestBody.setEmail(email);
        requestBody.setMobNo(mobileNumber);
        requestBody.setMAddrLine1(address1);
        requestBody.setMAddrLine2(address2);
        requestBody.setMAddrLine3(address3);
        requestBody.setMCity(city);
        requestBody.setMState(state);
        requestBody.setMCountry(country);
        requestBody.setMPostalCode(pin);
        requestBody.setUCC("");
        requestBody.setKYCVerified("");
        requestBody.setKYCVerified("false");
        requestBody.setKYCDescription("");
        requestBody.setTitle("MR.");
        requestBody.setMaritalStatus(maritalStatus);
        requestBody.setAddressType(addressType);
        requestBody.setErrorMessage("");
        requestBody.setBirthPlace(birthPlace);
        requestBody.setBirthCountry(birthCountry);
        requestBody.setPoliticallyExposed(politicalExposed);
        requestBody.setGrossannualincome(grossAnnualIncome);
        requestBody.setWealthSource(wealthSource);
        requestBody.setEstimatedFinancialGrowth(inr);
        requestBody.setOccupation("");
        requestBody.setOccupation_code("");
        requestBody.setPan_no1("");
        requestBody.setEmail1("");
        requestBody.setOccupation("2");
        requestBody.setPin(pin);
        requestBody.setIsofflineclient("0");
        requestBody.setClientIP("49.32.167.152");

        requestBody.setIsApplyNominee(isNominee);
        requestBody.setNomineeIsMinor1(isMinor);

        if (isNominee.equalsIgnoreCase("true")) {
            requestBody.setNomineeName1(nomineeName);
            requestBody.setDateOfBirth1(nomineeDob);
            requestBody.setNomineeRelationship1(nomineRelationship);
            requestBody.setNomineeShare1(nomineeShare);
            requestBody.setNomineeAddress1(nomineeAddress);

        }
        else {
            requestBody.setNomineeName1("");
            requestBody.setDateOfBirth1("");
            requestBody.setNomineeRelationship1("");
            requestBody.setNomineeShare1("0");
            requestBody.setNomineeAddress1("");
            requestBody.setNomineeIsMinor1(isMinor);
        }

        if (isMinor.equalsIgnoreCase("true")) {
            requestBody.setGuardianName1(guardianName);
        } else {
            requestBody.setGuardianName1("");
        }


        if (nominee2Status.equalsIgnoreCase("false")) {
            requestBody.setNomineeName2(nominee2Name);
            requestBody.setDateOfBirth2(nominee2Dob);
            requestBody.setNomineeRelationship2(nominee2Relationship);
            requestBody.setNomineeShare2(nominee2Share);
            requestBody.setNomineeAddress2(nominee2Address);
            requestBody.setNomineeIsMinor2(isMinor2);

        } else {
            requestBody.setNomineeName2("");
            requestBody.setDateOfBirth2("");
            requestBody.setNomineeRelationship2("");
            requestBody.setNomineeShare2("0");
            requestBody.setNomineeAddress2("");
            requestBody.setNomineeIsMinor2(isMinor2);
        }

        if (isMinor2.equalsIgnoreCase("true")) {
            requestBody.setGuardianName2(guardian2Name);
        } else {
            requestBody.setGuardianName2("");
        }

        if (nominee3Status.equalsIgnoreCase("false")) {
            requestBody.setNomineeName3(nominee3Name);
            requestBody.setDateOfBirth3(nominee3Dob);
            requestBody.setNomineeRelationship3(nominee3Relationship);
            requestBody.setNomineeShare3(nominee3Share);
            requestBody.setNomineeAddress3(nominee3Address);
            requestBody.setNomineeIsMinor3(isMinor3);

        } else {
            requestBody.setNomineeName3("");
            requestBody.setDateOfBirth3("");
            requestBody.setNomineeRelationship3("");
            requestBody.setNomineeShare3("0");
            requestBody.setNomineeAddress3("");
            requestBody.setNomineeIsMinor3(isMinor3);
        }

        if (isMinor3.equalsIgnoreCase("true")) {
            requestBody.setGuardianName3(guardian3Name);
        } else {
            requestBody.setGuardianName3("");
        }

        CallClientCreationActivationRequest model = new CallClientCreationActivationRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);


        SettingPreferences.setRequestUniqueIdentifier(getApplicationContext(), uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.CallClientCreationActivation(model).enqueue(new Callback<CallClientCreationActivationResponse>() {
            @Override
            public void onResponse(Call<CallClientCreationActivationResponse> call, Response<CallClientCreationActivationResponse> response) {
                util.showProgressDialog(NewWealthMgmtActivity.this, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));
                String ucc = BMSPrefs.getString(getApplicationContext(), "ucc");
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "account create successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), BOBActivity.class);
                    intent.putExtra("arg_customer_id", ucc);
                    intent.putExtra("arg_login_session_id", "");
                    intent.putExtra("arg_heartbeat_token", "");
                    intent.putExtra("arg_channel_id", "14");
                    startActivity(intent);
                    finish();
                    //  replaceFragment(new TermsAndConditionActivity());
//                    if (response.body().getClientCode().equalsIgnoreCase("1")) {
//
//                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CallClientCreationActivationResponse> call, Throwable t) {
                util.showProgressDialog(getApplicationContext(), false);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}