package com.dz.interactors.services

import com.dz.models.ArrayResponse
import com.dz.models.BaseResponse
import com.dz.models.requests.SubmissionRequest
import com.dz.models.responses.*
import com.dz.utilities.ServiceConfig
import io.reactivex.Observable
import retrofit2.http.*

interface IApiService {
    @POST(ServiceConfig.SIGN_IN)
    @FormUrlEncoded
    fun signIn(@Field("username") userName: String,
               @Field("password") password: String,
               @Field("grant_type") grantType: String,
               @Field("client_id") clientId: Int): Observable<BaseResponse<SignInResponse>>

    @GET(ServiceConfig.CONTESTS)
    fun getContests(@QueryMap params: HashMap<String, String>): Observable<BaseResponse<ArrayResponse<ContestResponse>>>

    @POST(ServiceConfig.HASHTAGS)
    @FormUrlEncoded
    fun createHashTag(@Field("name") name: String): Observable<BaseResponse<ArrayList<HashTagResponse?>>>

    @GET(ServiceConfig.HASHTAGS)
    fun getHashTags(@QueryMap params: HashMap<String, String>): Observable<BaseResponse<ArrayResponse<HashTagResponse>>>

    @GET(ServiceConfig.FRIENDS)
    fun getFriends(@QueryMap params: HashMap<String, String>): Observable<BaseResponse<ArrayResponse<MemberResponse>>>

    @POST(ServiceConfig.SUBMISSTION_FILE)
    fun submissionFile(@Body body: SubmissionRequest): Observable<BaseResponse<SubmisstionResponse>>

}