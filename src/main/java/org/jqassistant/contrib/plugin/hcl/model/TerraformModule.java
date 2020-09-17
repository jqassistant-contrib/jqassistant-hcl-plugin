package org.jqassistant.contrib.plugin.hcl.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * A container to group all objects within one terraform module.
 *
 * @author Matthias Kay
 * @since 1.0
 */
@Label("Module")
public interface TerraformModule extends TerraformBlock {
  enum FieldName implements TerraformModelField {
    NAME("name");

    private final String modelName;

    private FieldName(final String modelName) {
      this.modelName = modelName;
    }

    @Override
    public String getModelName() {
      return this.modelName;
    }
  }

  @Relation("DECLARES_INPUT_VARIABLE")
  List<TerraformInputVariable> getInputVariables();

  String getName();

  @Relation("DECLARES_OUTPUT_VARIABLE")
  List<TerraformOutputVariable> getOutputVariables();

  void setName(String name);
}