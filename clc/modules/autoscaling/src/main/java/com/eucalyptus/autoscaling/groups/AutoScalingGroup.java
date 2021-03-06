/*************************************************************************
 * Copyright 2009-2013 Eucalyptus Systems, Inc.
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
package com.eucalyptus.autoscaling.groups;

import static com.eucalyptus.autoscaling.common.AutoScalingMetadata.AutoScalingGroupMetadata;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Entity;
import com.eucalyptus.autoscaling.metadata.AbstractOwnedPersistent;
import com.eucalyptus.autoscaling.configurations.LaunchConfiguration;
import com.eucalyptus.util.OwnerFullName;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

/**
 *
 */
@Entity
@javax.persistence.Entity
@PersistenceContext( name = "eucalyptus_autoscaling" )
@Table( name = "metadata_auto_scaling_groups" )
@Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
public class AutoScalingGroup extends AbstractOwnedPersistent implements AutoScalingGroupMetadata {
  private static final long serialVersionUID = 1L;

  @Column( name = "metadata_max_size", nullable = false )
  private Integer maxSize;

  @Column( name = "metadata_min_size", nullable = false )
  private Integer minSize;

  @ManyToOne( optional = false )
  @JoinColumn( name = "metadata_launch_configuration_id" )
  @Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
  private LaunchConfiguration launchConfiguration;

  @Column( name = "metadata_default_cooldown", nullable = false  )
  private Integer defaultCooldown;

  @Column( name = "metadata_desired_capacity", nullable = false  )
  private Integer desiredCapacity;

  @Column( name = "metadata_health_check_grace_period" )
  private Integer healthCheckGracePeriod;

  @Column( name = "metadata_health_check_type", nullable = false  )
  @Enumerated( EnumType.STRING )
  private HealthCheckType healthCheckType;

  @Column( name = "metadata_status" )
  private String status;

  @ElementCollection
  @CollectionTable( name = "metadata_auto_scaling_group_availability_zones" )
  @Column( name = "metadata_availability_zone" )
  @JoinColumn( name = "metadata_auto_scaling_group_id" )
  @Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
  private Set<String> availabilityZones = Sets.newHashSet();
  
  //TODO:STEVE: instances?

  @ElementCollection
  @CollectionTable( name = "metadata_auto_scaling_group_termination_policies" )
  @Column( name = "metadata_termination_policy" )
  @JoinColumn( name = "metadata_auto_scaling_group_id" )
  @Enumerated( EnumType.STRING )
  @Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
  private Set<TerminationPolicyType> terminationPolicies = Sets.newHashSet();
  
  //TODO:STEVE: enabled metrics?

  @ElementCollection
  @CollectionTable( name = "metadata_auto_scaling_group_load_balancers" )
  @Column( name = "metadata_load_balancer_name" )
  @JoinColumn( name = "metadata_auto_scaling_group_id" )
  @Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
  private Set<String> loadBalancerNames = Sets.newHashSet(); 
  
  //TODO:STEVE: tags
  
  //TODO:STEVE: suspendedProcesses?
  
  //TODO:STEVE: include unsupported properties -> placementGroup, vpcZoneIdentifier

  protected AutoScalingGroup() {
  }

  protected AutoScalingGroup( final OwnerFullName owner ) {
    super( owner );
  }

  protected AutoScalingGroup( final OwnerFullName owner, final String displayName ) {
    super( owner, displayName );
  }

  public String getAutoScalingGroupName() {
    return getDisplayName();
  }

  public Integer getMaxSize() {
    return maxSize;
  }

  public void setMaxSize( final Integer maxSize ) {
    this.maxSize = maxSize;
  }

  public Integer getMinSize() {
    return minSize;
  }

  public void setMinSize( final Integer minSize ) {
    this.minSize = minSize;
  }

  public LaunchConfiguration getLaunchConfiguration() {
    return launchConfiguration;
  }

  public void setLaunchConfiguration( final LaunchConfiguration launchConfiguration ) {
    this.launchConfiguration = launchConfiguration;
  }

  public Integer getDefaultCooldown() {
    return defaultCooldown;
  }

  public void setDefaultCooldown( final Integer defaultCooldown ) {
    this.defaultCooldown = defaultCooldown;
  }

  public Integer getDesiredCapacity() {
    return desiredCapacity;
  }

  public void setDesiredCapacity( final Integer desiredCapacity ) {
    this.desiredCapacity = desiredCapacity;
  }

  public Integer getHealthCheckGracePeriod() {
    return healthCheckGracePeriod;
  }

  public void setHealthCheckGracePeriod( final Integer healthCheckGracePeriod ) {
    this.healthCheckGracePeriod = healthCheckGracePeriod;
  }

  public HealthCheckType getHealthCheckType() {
    return healthCheckType;
  }

  public void setHealthCheckType( final HealthCheckType healthCheckType ) {
    this.healthCheckType = healthCheckType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus( final String status ) {
    this.status = status;
  }

  public Set<String> getAvailabilityZones() {
    return availabilityZones;
  }

  public void setAvailabilityZones( final Set<String> availabilityZones ) {
    this.availabilityZones = availabilityZones;
  }

  public Set<TerminationPolicyType> getTerminationPolicies() {
    return terminationPolicies;
  }

  public void setTerminationPolicies( final Set<TerminationPolicyType> terminationPolicies ) {
    this.terminationPolicies = terminationPolicies;
  }

  public Set<String> getLoadBalancerNames() {
    return loadBalancerNames;
  }

  public void setLoadBalancerNames( final Set<String> loadBalancerNames ) {
    this.loadBalancerNames = loadBalancerNames;
  }

  @Override
  public String getArn() {
    return String.format(
        "arn:aws:autoscaling::%1s:autoScalingGroup:%2s:autoScalingGroupName/%3s",
        getOwnerAccountNumber(),
        getNaturalId(),
        getDisplayName() );
  }

  /**
   * Create an example AutoScalingGroup for the given owner. 
   *
   * @param ownerFullName The owner
   * @return The example
   */
  public static AutoScalingGroup withOwner( final OwnerFullName ownerFullName ) {
    return new AutoScalingGroup( ownerFullName );
  }

  /**
   * Create an example AutoScalingGroup for the given owner and name. 
   *
   * @param ownerFullName The owner
   * @param name The name
   * @return The example
   */
  public static AutoScalingGroup named( final OwnerFullName ownerFullName,
                                           final String name ) {
    return new AutoScalingGroup( ownerFullName, name );
  }

  public static AutoScalingGroup withId( final String id ) {
    final AutoScalingGroup example = new AutoScalingGroup();
    example.setId( id);
    return example;
  }

  public static AutoScalingGroup withUuid( final String uuid ) {
    final AutoScalingGroup example = new AutoScalingGroup();
    example.setNaturalId( uuid );
    return example;
  }

  public static AutoScalingGroup create( final OwnerFullName ownerFullName,
                                         final String name,
                                         final LaunchConfiguration launchConfiguration,
                                         final Integer minSize,
                                         final Integer maxSize ) {
    final AutoScalingGroup autoScalingGroup = new AutoScalingGroup( ownerFullName, name );
    autoScalingGroup.setLaunchConfiguration( launchConfiguration );
    autoScalingGroup.setMinSize( minSize );
    autoScalingGroup.setMaxSize( maxSize );
    return autoScalingGroup;
  }

  protected static abstract class BaseBuilder<T extends BaseBuilder<T>> {
    private OwnerFullName ownerFullName;
    private String name;
    private Integer minSize;
    private Integer maxSize;
    private Integer defaultCooldown;
    private Integer desiredCapacity;
    private Integer healthCheckGracePeriod;
    private HealthCheckType healthCheckType;
    private LaunchConfiguration launchConfiguration;
    private Set<String> availabilityZones = Sets.newHashSet();
    private Set<TerminationPolicyType> terminationPolicies = Sets.newHashSet();
    private Set<String> loadBalancerNames = Sets.newHashSet();

    BaseBuilder( final OwnerFullName ownerFullName,
                 final String name,
                 final LaunchConfiguration launchConfiguration,
                 final Integer minSize,
                 final Integer maxSize ) {
      this.ownerFullName = ownerFullName;
      this.name = name;
      this.launchConfiguration = launchConfiguration;
      this.minSize = minSize;
      this.maxSize = maxSize;
    }

    protected abstract T builder();

    public T withDefaultCooldown( final Integer defaultCooldown ) {
      this.defaultCooldown  = defaultCooldown;
      return builder();
    }

    public T withDesiredCapacity( final Integer desiredCapacity ) {
      this.desiredCapacity  = desiredCapacity;
      return builder();
    }

    public T withHealthCheckGracePeriod( final Integer healthCheckGracePeriod ) {
      this.healthCheckGracePeriod  = healthCheckGracePeriod;
      return builder();
    }

    public T withHealthCheckType( final HealthCheckType healthCheckType ) {
      this.healthCheckType  = healthCheckType;
      return builder();
    }

    public T withAvailabilityZones( final Collection<String> availabilityZones ) {
      if ( availabilityZones != null ) {
        this.availabilityZones.addAll( availabilityZones );
      }
      return builder();
    }

    public T withTerminationPolicyTypes( final Collection<TerminationPolicyType> terminationPolicies ) {
      if ( terminationPolicies != null ) {
        this.terminationPolicies.addAll( terminationPolicies );
      }
      return builder();
    }

    public T withLoadBalancerNames( final Collection<String> loadBalancerNames ) {
      if ( loadBalancerNames != null ) {
        this.loadBalancerNames.addAll( loadBalancerNames );
      }
      return builder();
    }

    //TODO:STEVE: verify default values
    protected AutoScalingGroup build() {
      final AutoScalingGroup group =
          AutoScalingGroup.create( ownerFullName, name, launchConfiguration, minSize, maxSize );
      group.setDefaultCooldown( Objects.firstNonNull( defaultCooldown, 300 ) ); 
      group.setDesiredCapacity( Objects.firstNonNull( desiredCapacity, minSize ) );
      group.setHealthCheckGracePeriod( healthCheckGracePeriod );
      group.setHealthCheckType( Objects.firstNonNull( healthCheckType, HealthCheckType.EC2 ) );
      group.setAvailabilityZones( availabilityZones );
      group.setTerminationPolicies( terminationPolicies.isEmpty() ? 
          Collections.singleton(TerminationPolicyType.Default) : 
          terminationPolicies );
      group.setLoadBalancerNames( loadBalancerNames );
      return group;
    }
  }  
}
