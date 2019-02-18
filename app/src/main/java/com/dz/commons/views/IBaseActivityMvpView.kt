package com.dz.commons.views

import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent

interface IBaseActivityMvpView : IBaseMvpView, LifecycleProvider<ActivityEvent>