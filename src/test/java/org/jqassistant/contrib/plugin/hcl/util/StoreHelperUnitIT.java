package org.jqassistant.contrib.plugin.hcl.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.jqassistant.contrib.plugin.hcl.model.TerraformBlock;
import org.jqassistant.contrib.plugin.hcl.model.TerraformLogicalModule;
import org.jqassistant.contrib.plugin.hcl.model.TerraformModelField;
import org.jqassistant.contrib.plugin.hcl.model.TerraformProviderResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import com.google.common.collect.ImmutableMap;

public class StoreHelperUnitIT extends AbstractPluginIT {
  @BeforeEach
  public void beginTransaction() {
    this.store.beginTransaction();
  }

  @Test
  public void shouldReturnSameObject_whenCreateOrRetrieveObject_givenObjectExists() {
    // given
    final Map<TerraformModelField, String> givenSearchCriteria = ImmutableMap
        .of(TerraformLogicalModule.FieldName.FULL_QUALIFIED_NAME, "XXX");

    // when
    final StoreHelper s = new StoreHelper(this.store);

    final TerraformLogicalModule actualCreated = s.createOrRetrieveObject(givenSearchCriteria,
        TerraformLogicalModule.class);
    actualCreated.setFullQualifiedName("XXX");

    final TerraformLogicalModule actualFound = s.createOrRetrieveObject(givenSearchCriteria,
        TerraformLogicalModule.class);

    // then
    assertThat(actualCreated.getFullQualifiedName()).isEqualTo(actualFound.getFullQualifiedName());
  }

  @Test
  public void shouldReturnTheSpecializedDescriptor_whenCreateOrRetrieveObject_givenObjectWithFullQualifiedNameExistsAsTerraformBlock() {
    // given
    final Map<TerraformModelField, String> givenSearchCriteria = ImmutableMap
        .of(TerraformBlock.FieldName.FULL_QUALIFIED_NAME, "XXX");

    // when
    final StoreHelper s = new StoreHelper(this.store);

    final TerraformBlock actualCreated = s.createOrRetrieveObject(givenSearchCriteria, TerraformBlock.class);

    final TerraformProviderResource actualConverted = s.createOrRetrieveObject(givenSearchCriteria,
        TerraformProviderResource.class);

    // then
    final Object idCreated = actualCreated.getId();
    final Object idConverted = actualConverted.getId();

    assertThat(idCreated).isEqualTo(idConverted);
  }

  @Test
  public void shouldSetTheFullQualifiedName_whenCreateOrRetrieveObject() {
    // given
    final Map<TerraformModelField, String> givenSearchCriteria = ImmutableMap
        .of(TerraformBlock.FieldName.FULL_QUALIFIED_NAME, "XXX");

    // when
    final StoreHelper s = new StoreHelper(this.store);

    final TerraformBlock actualCreated = s.createOrRetrieveObject(givenSearchCriteria, TerraformBlock.class);

    // then
    assertThat(actualCreated.getFullQualifiedName()).isEqualTo("XXX");
  }
}
