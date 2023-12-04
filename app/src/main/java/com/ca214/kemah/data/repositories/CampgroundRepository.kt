package com.ca214.kemah.data.repositories

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ca214.kemah.LoginActivity
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.data.models.requests.CampgroundRequest
import com.ca214.kemah.data.models.responses.CampgroundResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class CampgroundRepository {
    private val apiService = ApiConfig.instanceCampgroundService

    fun getCampgrounds(): LiveData<List<Campground>> {
        val data = MutableLiveData<List<Campground>>()

        apiService.getCampgrounds().enqueue(object : Callback<List<CampgroundResponse>> {
            override fun onResponse(call: Call<List<CampgroundResponse>>, response: Response<List<CampgroundResponse>>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (!responseData.isNullOrEmpty()) {
                        val result = responseData.map { campground ->
                            Campground(
                                id = campground.id,
                                name = campground.name,
                                location = campground.location,
                                address = campground.address,
                                price = campground.price,
                                imageUrl = campground.imageUrl,
                                description = campground.description,
                                latitude = campground.latitude,
                                longitude = campground.longitude,
                                creatorId = campground.creatorId,
                                creatorUsername = campground.creatorUsername
                            )
                        }
                        data.value = result
                    }
                }
            }

            override fun onFailure(call: Call<List<CampgroundResponse>>, t: Throwable) {
                val test = t.localizedMessage
                val x = test
            }
        })

        return data
    }

    fun addCampground(campground: Campground): LiveData<Campground> {
        val data = MutableLiveData<Campground>()
        val campgroundRequest = CampgroundRequest (
            name = campground.name,
            location = campground.location,
            address = campground.address,
            price = campground.price,
            imageUrl = campground.imageUrl,
            description = campground.description,
            latitude = campground.latitude,
            longitude = campground.longitude,
        )

        apiService.addCampground(campgroundRequest).enqueue(object : Callback<CampgroundResponse> {
            override fun onResponse(call: Call<CampgroundResponse>, response: Response<CampgroundResponse>) {
                if (response.isSuccessful) {
                    val campgroundResponse = response.body()
                    if (campgroundResponse != null) {
                        data.value = Campground(
                            id = campgroundResponse.id,
                            name = campgroundResponse.name,
                            location = campgroundResponse.location,
                            address = campgroundResponse.address,
                            price = campgroundResponse.price,
                            imageUrl = campgroundResponse.imageUrl,
                            description = campgroundResponse.description,
                            latitude = campgroundResponse.latitude,
                            longitude = campgroundResponse.longitude,
                            creatorId = campgroundResponse.creatorId,
                            creatorUsername = campgroundResponse.creatorUsername
                        );
                    }
                }
            }

            override fun onFailure(call: Call<CampgroundResponse>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }

    fun updateCampground(id: Int, campground: Campground): LiveData<Campground> {
        val data = MutableLiveData<Campground>()
        val campgroundId = campground.id;

        if (campgroundId != null) {

            val campgroundRequest = CampgroundRequest (
                name = campground.name,
                location = campground.location,
                address = campground.address,
                price = campground.price,
                imageUrl = campground.imageUrl,
                description = campground.description,
                latitude = campground.latitude,
                longitude = campground.longitude,
            )

            apiService.updateCampground(campgroundId, campgroundRequest).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        data.value = campground
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    data.value = null
                }
            })
        }

        return data
    }

    fun deleteCampground(id: UUID): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        apiService.deleteCampground(id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                data.value = false
            }
        })

        return data
    }
}