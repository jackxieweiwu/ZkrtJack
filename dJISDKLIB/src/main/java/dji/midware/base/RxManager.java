package dji.midware.base;


import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;

/**
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 * Created by jack_xie on 16/4/28.
 */
public class RxManager {

    // public RxBus mRxBus = RxBus.$();
    // private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者


//    @Deprecated
//    public void on(String eventName, Action1<Object> action1) {
//        Observable<?> mObservable = mRxBus.register(eventName);
//        mObservables.put(eventName, mObservable);
//        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action1, (e) -> e.printStackTrace()));
//        if (mRxBus.mStickyEventList.containsKey(eventName))
//            ((Subject) mObservable).onNext(mRxBus.mStickyEventList.get(eventName));
//    }

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
        //for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
        //    mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    // @Deprecated
    //public void post(Object tag, Object content) {
    //    mRxBus.post(tag, content);
    // }
}
