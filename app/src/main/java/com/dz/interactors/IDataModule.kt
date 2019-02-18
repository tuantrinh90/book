package com.dz.interactors

import com.dz.interactors.services.IApiService
import com.dz.interactors.services.IFileApiService
import com.dz.interactors.services.ILongApiService
import com.dz.interactors.databases.IDbModule

interface IDataModule {
    var apiService: IApiService

    var longApiService: ILongApiService

    var fileApiService: IFileApiService

    var dbModule: IDbModule
}
