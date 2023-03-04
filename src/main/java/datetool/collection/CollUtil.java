package datetool.collection;

import datetool.comparator.CompareUtil;
import datetool.exceptions.UtilException;
import datetool.lang.Assert;

import java.util.*;

/**
 * 集合相关工具类
 * <p>
 * 此工具方法针对{@link Collection}及其实现类封装的工具。
 * <p>
 * 由于{@link Collection} 实现了{@link Iterable}接口，因此部分工具此类不提供，而是在{@link IterUtil} 中提供
 *
 * @author xiaoleilu
 * @see IterUtil
 * @since 3.1.1
 */
public class CollUtil {


    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterable    {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterator, CharSequence)
     */
    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        if (null == iterable) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction);
    }

    // ----------------------------------------------------------------------------------------------- new HashSet


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
        return ListUtil.toList(iterable);
    }

    // ----------------------------------------------------------------------new LinkedList


    /**
     * 创建新的集合对象
     *
     * @param <T>            集合类型
     * @param collectionType 集合类型
     * @return 集合类型对应的实例
     * @since 3.0.8
     */
    public static <T> Collection<T> create(Class<?> collectionType) {
        return create(collectionType, null);
    }

    /**
     * 创建新的集合对象，返回具体的泛型集合
     *
     * @param <T>            集合元素类型
     * @param collectionType 集合类型，rawtype 如 ArrayList.class, EnumSet.class ...
     * @param elementType    集合元素类型
     * @return 集合类型对应的实例
     * @since v5
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> create(Class<?> collectionType, Class<T> elementType) {
        final Collection<T> list;
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            // 抽象集合默认使用ArrayList
            list = new ArrayList<>();
        }

        // Set
        else if (collectionType.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet<>();
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet<>((o1, o2) -> {
                // 优先按照对象本身比较，如果没有实现比较接口，默认按照toString内容比较
                if (o1 instanceof Comparable) {
                    return ((Comparable<T>) o1).compareTo(o2);
                }
                return CompareUtil.compare(o1.toString(), o2.toString());
            });
        } else if (collectionType.isAssignableFrom(EnumSet.class)) {
            list = (Collection<T>) EnumSet.noneOf(Assert.notNull((Class<Enum>) elementType));
        }

        // List
        else if (collectionType.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            list = new LinkedList<>();
        }

        // Others，直接实例化
        else {
            try {
                list = (Collection<T>) ReflectUtil.newInstance(collectionType);
            } catch (final Exception e) {
                // 无法创建当前类型的对象，尝试创建父类型对象
                final Class<?> superclass = collectionType.getSuperclass();
                if (null != superclass && collectionType != superclass) {
                    return create(superclass);
                }
                throw new UtilException(e);
            }
        }
        return list;
    }


    // ---------------------------------------------------------------------- isEmpty

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
