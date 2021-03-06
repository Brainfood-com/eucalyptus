/*************************************************************************
 * Copyright 2009-2012 Eucalyptus Systems, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Please contact Eucalyptus Systems, Inc., 6755 Hollister Ave., Goleta
 * CA 93117, USA or visit http://www.eucalyptus.com/licenses/ if you need
 * additional information or have any questions.
 ************************************************************************/
package com.eucalyptus.autoscaling.common;

import static com.eucalyptus.autoscaling.common.AutoScalingMetadata.AutoScalingMetadataWithResourceName;
import java.util.Collection;
import com.eucalyptus.util.CollectionUtils;
import com.eucalyptus.util.RestrictedTypes;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 *
 */
public class AutoScalingMetadatas {
  
  public static <T extends AutoScalingMetadata> Function<T, String> toDisplayName( ) {
    return new Function<T, String>( ) {
      @Override
      public String apply( T metadata ) {
        return metadata == null ? null : metadata.getDisplayName( );
      }
    };
  }
  
  public static <T extends AutoScalingMetadataWithResourceName> Function<T, String> toArn( ) {
    return new Function<T, String>( ) {
      @Override
      public String apply( T metadata ) {
        return metadata == null ? null : metadata.getArn();
      }
    };
  }
  
  public static <T extends AutoScalingMetadata> Predicate<T> filterById( final Collection<String> requestedIdentifiers ) {
    return filterByProperty( requestedIdentifiers, toDisplayName() );
  }

  public static <T extends AutoScalingMetadataWithResourceName> Predicate<T> filterByArn( final Collection<String> requestedArns ) {
    return filterByProperty( requestedArns, toArn() );
  }

  public static <T extends AutoScalingMetadata> Predicate<T> filterByProperty( final String requestedValue,
                                                                               final Function<? super T,String> extractor ) {
    return filterByProperty( CollectionUtils.<String>listUnit().apply( requestedValue ), extractor );
  }

  public static <T extends AutoScalingMetadata> Predicate<T> filterByProperty( final Collection<String> requestedValues,
                                                                               final Function<? super T,String> extractor ) {
    return new Predicate<T>( ) {
      @Override
      public boolean apply( T input ) {
        return requestedValues == null || requestedValues.isEmpty( ) || requestedValues.contains( extractor.apply( input ) );
      }
    };
  }

  public static <T extends AutoScalingMetadata> Predicate<T> filterPrivilegesById( final Collection<String> requestedIdentifiers ) {
    return Predicates.and( filterById( requestedIdentifiers ), filterPrivileged() );
  }
  
  public static <T extends AutoScalingMetadataWithResourceName> Predicate<T> filterPrivilegesByIdOrArn( final Collection<String> requestedItems ) {
    final Collection<String> names = AutoScalingResourceName.simpleNames( requestedItems );
    final Collection<String> arns =  AutoScalingResourceName.arns( requestedItems );
    return Predicates.and(
        !arns.isEmpty() && !names.isEmpty() ? 
          Predicates.<T>or(
              AutoScalingMetadatas.<T>filterById( names ),
              AutoScalingMetadatas.<T>filterByArn( arns ) ) :
          !arns.isEmpty() ?
              AutoScalingMetadatas.<T>filterByArn( arns ) :
              AutoScalingMetadatas.<T>filterById( names ),
        filterPrivileged() );    
  }
  
  public static <T extends AutoScalingMetadata> Predicate<T> filterPrivileged() {
    return RestrictedTypes.filterPrivileged();
  }

}
