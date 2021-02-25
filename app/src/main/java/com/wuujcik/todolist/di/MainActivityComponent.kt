package com.wuujcik.todolist.di

import com.wuujcik.todolist.ui.MainActivity
import com.wuujcik.todolist.ui.details.DetailsFragment
import com.wuujcik.todolist.ui.list.ListFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailsFragment)
}
