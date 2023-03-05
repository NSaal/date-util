package datetool.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 集合相关工具类
 * <p>
 * 此工具方法针对{@link Collection}及其实现类封装的工具。
 * <p>
 * 由于{@link Collection} 实现了{@link Iterable}接口，因此部分工具此类不提供，而是在 IterUtil 中提供
 *
 * @author xiaoleilu
 * @since 3.1.1
 */
public class CollUtil {

    // ----------------------------------------------------------------------------------------------- List

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return ArrayList对象
     * @since 3.1.0
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> iterable) {
        ArrayList<T> arrayList = new ArrayList<>();
        if (null == iterable) {
            return arrayList;
        }
        for (T t : iterable) {
            arrayList.add(t);
        }
        return arrayList;
    }

}
