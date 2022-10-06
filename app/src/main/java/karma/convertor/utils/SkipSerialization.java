package karma.convertor.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Custom annotation to skip the serialization of some fields. It used to store some data as gson in the preference,
 so that all the data is not stored in it.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SkipSerialization {

}