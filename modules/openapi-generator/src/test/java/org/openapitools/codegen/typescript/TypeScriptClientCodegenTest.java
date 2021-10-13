package org.openapitools.codegen.typescript;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.*;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.languages.TypeScriptClientCodegen;
import org.openapitools.codegen.utils.ModelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

public class TypeScriptClientCodegenTest {
    @Test
    public void getTypeDeclarationTest() {
        Schema<?> childSchema = new ArraySchema().items(new StringSchema());

        OpenAPI api = TestUtils.createOpenAPI();
        api.getComponents().addSchemas("Child", childSchema);

        TypeScriptClientCodegen codegen = new TypeScriptClientCodegen();
        codegen.setOpenAPI(api);

        // Cf. issue #4968: Array of Alias of Array
        Schema<?> parentSchema = new ArraySchema().items(
            new Schema().$ref("#/components/schemas/Child")
        );

        ModelUtils.setGenerateAliasAsModel(false);
        Assert.assertEquals(codegen.getTypeDeclaration(parentSchema), "Array<Array<string>>");

        ModelUtils.setGenerateAliasAsModel(true);
        Assert.assertEquals(codegen.getTypeDeclaration(parentSchema), "Array<Child>");

        // Same for Map
        parentSchema = new MapSchema().additionalProperties(new Schema().$ref("#/components/schemas/Child"));

        ModelUtils.setGenerateAliasAsModel(false);
        Assert.assertEquals(codegen.getTypeDeclaration(parentSchema), "{ [key: string]: Array<string>; }");

        ModelUtils.setGenerateAliasAsModel(true);
        Assert.assertEquals(codegen.getTypeDeclaration(parentSchema), "{ [key: string]: Child; }");
    }

    @Test
    public void testNullableObjectProperty() {
        final OpenAPI openAPI = TestUtils.parseFlattenSpec("src/test/resources/3_0/issue_10593.yaml");
        final TypeScriptClientCodegen codegen = new TypeScriptClientCodegen();

        codegen.preprocessOpenAPI(openAPI);

        Schema test1 = openAPI.getComponents().getSchemas().get("ModelWithNullableObjectProperty");
        CodegenModel cm1 = codegen.fromModel("ModelWithNullableObjectProperty", test1);


        // codegen.processm()
        Map<String, Object> models = Collections.singletonMap("models", Collections.singletonList(Collections.singletonMap("model", cm1)));
        // codegen.postProcessAllModels(models);
        // We need to postProcess the model for enums to be processed
        // codegen.postProcessModels(models);
    }
}
