package com.ca214.kemah.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.data.models.CampgroundDetail
import com.ca214.kemah.data.models.Comment
import com.ca214.kemah.data.models.Creator
import com.ca214.kemah.data.models.requests.CampgroundRequest
import com.ca214.kemah.data.models.responses.CampgroundDetailResponse
import com.ca214.kemah.data.models.responses.CampgroundResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class CampgroundRepository {
    private val apiService = ApiConfig.instanceCampgroundRetrofit

    fun getAllCampgrounds() : LiveData<List<Campground>> {
        val data = MutableLiveData<List<Campground>>()
        try {
            // API call untuk GET semua data campgrounds
            apiService.getAllCampgrounds().enqueue(object : Callback<List<CampgroundResponse>> {
                override fun onResponse(
                    call: Call<List<CampgroundResponse>>,
                    response: Response<List<CampgroundResponse>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (!responseBody.isNullOrEmpty()) {
                            val result = responseBody.map { campground ->
                                Campground(
                                    id = campground.id,
                                    name = campground.name,
                                    location = campground.location,
                                    address = campground.address,
                                    price = campground.price,
                                    description = campground.description,
                                    imageUrl = campground.imageURL,
                                    longitude = campground.longitude,
                                    latitude = campground.latitude,
                                    creatorId = campground.creatorID,
                                    creatorUsername = campground.creatorUsername
                                )
                            }
                            data.value = result
                        }
                    }
                }

                override fun onFailure(call: Call<List<CampgroundResponse>>, t: Throwable) {
                    Log.e("Load data campground Error", t.localizedMessage)
                }
            })
        } catch (er: Exception) {
            Log.e("Campground Repository Get All Error", er.localizedMessage)
        }
        return data;
    }

    fun getCampgroundById(id: UUID) : LiveData<CampgroundDetail> {
        val data = MutableLiveData<CampgroundDetail>()

        apiService.getCampgroundById(id).enqueue(object : Callback<CampgroundDetailResponse> {
            override fun onResponse(
                call: Call<CampgroundDetailResponse>,
                response: Response<CampgroundDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val campground = response.body()
                    if (campground != null) {
                        data.value = CampgroundDetail(
                            id = campground.id,
                            name = campground.name,
                            location = campground.location,
                            address = campground.address,
                            price = campground.price,
                            description = campground.description,
                            imageUrl = campground.imageURL,
                            longitude = campground.longitude,
                            latitude = campground.latitude,
                            creator = Creator(
                                id = campground.creator.id,
                                username = campground.creator.username,
                                email = campground.creator.email,
                                isAdmin = campground.creator.isAdmin
                            ),
                            comments = campground.comments.map {
                                comment -> Comment(
                                    id = comment.id,
                                    creatorId = comment.creatorId,
                                    creatorUsername = comment.creatorUsername,
                                    content = comment.content,
                                )
                            }
                        )
                    }
                }
            }

            override fun onFailure(call: Call<CampgroundDetailResponse>, t: Throwable) {
                data.value = null
                Log.e("Campground Repository Get By ID", t.localizedMessage)
            }

        })

        return data
    }

    fun addCampground(campground: CampgroundRequest): LiveData<Campground> {
        val data = MutableLiveData<Campground>()

        apiService.addCampground(campground).enqueue(object : Callback<CampgroundResponse> {
            override fun onResponse(
                call: Call<CampgroundResponse>,
                response: Response<CampgroundResponse>
            ) {
                if (response.isSuccessful) {
                    val campgroundResponse = response.body()
                    if (campgroundResponse != null) {
                        data.value = Campground(
                            id = campgroundResponse.id,
                            name = campgroundResponse.name,
                            location = campgroundResponse.location,
                            address = campgroundResponse.address,
                            price = campgroundResponse.price,
                            imageUrl = campgroundResponse.imageURL,
                            description = campgroundResponse.description,
                            longitude = campgroundResponse.longitude,
                            latitude = campgroundResponse.latitude,
                            creatorId = campgroundResponse.creatorID,
                            creatorUsername = campgroundResponse.creatorUsername
                        )
                    }
                }
            }

            override fun onFailure(call: Call<CampgroundResponse>, t: Throwable) {
                data.value = null
            }

        })

        return data
    }

    fun updateCampground(id: UUID, updatedCampground: CampgroundRequest) : LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        apiService.updateCampground(id, updatedCampground).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    data.value = response.isSuccessful
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    data.value = false
                    Log.e("Campground Repository Update", t.localizedMessage)
                }

            }
        )

        return data
    }

    fun deleteCampground(id: UUID) : LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        apiService.deleteCampground(id).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    data.value = response.isSuccessful
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    data.value = false
                    Log.e("Campground Repository Delete", t.localizedMessage)
                }

            }
        )

        return data
    }
}