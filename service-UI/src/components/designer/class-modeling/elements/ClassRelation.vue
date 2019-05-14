<template>
  <div>
    <edge-element
      selectable
      connectable
      deletable
      :id="null"
      :vertices.sync="vertices"
      :from.sync="value.from"
      :to.sync="value.to"
      :_style="style_"
      :label="value.name"
      v-on:removeShape="onRemoveShape"
      v-on:dblclick="showProperty"
    >
    </edge-element>

    <modeling-property-panel
      :drawer.sync="drawer"
      v-model="value"
    >
      <template slot="properties-contents">
        <md-input-container>
          <label>릴레이션 명</label>
          <md-input type="text"
                    v-model="value.name"></md-input>
        </md-input-container>

        <md-input-container>
          <label>Relation Type</label>
          <md-select
            v-model="value._type">
            <md-option value="org.uengine.uml.model.Association">Association</md-option>
            <md-option value="org.uengine.uml.model.Aggregation">Aggregation</md-option>
            <md-option value="org.uengine.uml.model.Generalization">Generalization(Inheritance)</md-option>
          </md-select>
        </md-input-container>

        <div v-if="value._type == 'org.uengine.uml.model.Aggregation'">
          <md-input-container>
            <label>Source Multiplicity</label>
            <md-select
              v-model="value.sourceMultiplicity">
              <md-option value="1">1</md-option>
              <md-option value="1..n">1..n</md-option>
              <md-option value="0..n">0..n</md-option>
              <md-option value="0..1">0..1</md-option>
            </md-select>
          </md-input-container>

          <md-input-container>
            <label>Target Multiplicity</label>
            <md-select
              v-model="value.targetMultiplicity">
              <md-option value="1">1</md-option>
              <md-option value="1..n">1..n</md-option>
              <md-option value="0..n">0..n</md-option>
              <md-option value="0..1">0..1</md-option>
            </md-select>
          </md-input-container>


        </div>

        <md-input-container>
          <label>Source Alias</label>
          <md-input type="text"
                    v-model="value.sourceName"></md-input>
        </md-input-container>

        <md-input-container>
          <label>Target Alias</label>
          <md-input type="text"
                    v-model="value.targetName"></md-input>
        </md-input-container>

      </template>
    </modeling-property-panel>
  </div>
</template>

<script>
  import Element from '../../modeling/Element'

  export default {
    mixins: [Element],
    name: 'class-relation',
    props: {
      value: Object
    },

    created: function () {
      if (this.value && this.value.relationView) {

        this.value.from = this.value.relationView.from;
        this.value.to = this.value.relationView.to;

      }

      if (!OG.marker.GeneralizationMarker) {
        OG.marker.GeneralizationMarker = function () {
          OG.marker.GeneralizationMarker.superclass.call(this);
          this.MARKER_ID = 'OG.marker.GeneralizationMarker';
        };
        OG.marker.GeneralizationMarker.prototype = new OG.marker.IMarker();
        OG.marker.GeneralizationMarker.superclass = OG.marker.IMarker;
        OG.marker.GeneralizationMarker.prototype.constructor = OG.marker.GeneralizationMarker;
        OG.GeneralizationMarker = OG.marker.GeneralizationMarker;
        OG.marker.GeneralizationMarker.prototype.createMarker = function () {
          var geom1, geom2, geomCollection = [];
          if (this.geom) {
            return this.geom;
          }
          geom1 = new OG.geometry.Polygon([[20, 15], [50, 51], [20, 85]]);
          geom1.style = new OG.geometry.Style({
            'stroke-width': 0.8,
            fill: '#ffffff',
            'fill-opacity': 1
          });
          geomCollection.push(geom1);
          this.geom = new OG.geometry.GeometryCollection(geomCollection);
          return this.geom;
        };

      }

      if (!OG.marker.AggregationMarker) {
        OG.marker.AggregationMarker = function () {
          OG.marker.AggregationMarker.superclass.call(this);
          this.MARKER_ID = 'OG.marker.AggregationMarker';
        };
        OG.marker.AggregationMarker.prototype = new OG.marker.IMarker();
        OG.marker.AggregationMarker.superclass = OG.marker.IMarker;
        OG.marker.AggregationMarker.prototype.constructor = OG.marker.AggregationMarker;
        OG.GeneralizationMarker = OG.marker.AggregationMarker;
        OG.marker.AggregationMarker.prototype.createMarker = function () {
          var geom1, geom2, geomCollection = [];
          if (this.geom) {
            return this.geom;
          }
          geom1 = new OG.geometry.Polygon([[20, 15], [40, 51], [20, 85], [0, 51]]);
          geom1.style = new OG.geometry.Style({
            'stroke-width': 0.8,
            fill: '#ffffff',
            'fill-opacity': 1
          });
          geomCollection.push(geom1);
          this.geom = new OG.geometry.GeometryCollection(geomCollection);
          return this.geom;
        };

      }


    },


    computed: {
      defaultStyle() {
        return {}
      },
      style_() {

        if (this.value._type == "org.uengine.uml.model.Aggregation") {
          var style = {
            "arrow-end": ("01".indexOf(this.value.sourceMultiplicity) > -1 ? "none" : "diamond"),
            "arrow-start": ("01".indexOf(this.value.targetMultiplicity) > -1 ? "none" : "diamond")
          };

          if ("01".indexOf(this.value.sourceMultiplicity) == -1) {
            if (!style.marker) style.marker = {};

            style.marker.end = {
              'id': 'OG.marker.AggregationMarker',
              'size': [10, 8]
            }
          }

          if ("01".indexOf(this.value.targetMultiplicity) == -1) {
            if (!style.marker) style.marker = {};

            style.marker.start = {
              'id': 'OG.marker.AggregationMarker',
              'size': [10, 8]
            }
          }

          return style;

        } else if (this.value._type == "org.uengine.uml.model.Generalization") {

          return ({
            "arrow-end": "none",
            'marker': {
              'start': {
                'id': 'OG.marker.GeneralizationMarker',
                'size': [8, 8]
              }
            }

          });

        }
      },
      type() {
        return 'org.uengine.uml.model.Aggregation'
      },
      createNew(from, to, vertices) {
        return {
          _type: this.type(),
          sourceClassName: from.name,
          targetClassName: to.name,
          from: from.elementView.id,
          to: to.elementView.id,
          selected: false,
          relationView: {
            style: JSON.stringify({
              "arrow-start": "none",
              "arrow-end": "none"
            }),
            value: vertices,
            from: from.elementView.id,
            to: to.elementView.id,
            needReconnect: true
          },
          sourceMultiplicity: 1,
          targetMultiplicity: 1
        }
      },
      vertices: {
        get: function () {
          var style;
          try {
            return JSON.parse(this.value.relationView.value);
          } catch (e) {
            return null;
          }
        },
        set: function (val) {
          this.value.relationView.value = JSON.stringify(val);
        }
      }
    },
    data: function () {
      return {
        otherwise: false
      };
    },
    watch: {
      value: {
        deep: true,

        handler: function () {
          this.createAssociationFields()
          this.$emit("input", this.value)
        }

      }

    },
    mounted: function () {
    },
    methods: {

      createAssociationFields() {

        var me = this;

        if (this.value._type != "org.uengine.uml.model.Aggregation") return;

        if (this.definition.classDefinitions) {

          var classDefinitionByName = {};

          this.definition.classDefinitions[1].forEach(function (classDefinition) {
            var direction = null;

            if (classDefinition.name == me.value.sourceClassName)
              classDefinitionByName['source'] = classDefinition;
            else if (classDefinition.name == me.value.targetClassName)
              classDefinitionByName['target'] = classDefinition;

          });

          if (!classDefinitionByName["source"] || !classDefinitionByName["target"])
            return;

          var associationFields = {};

          ["source", "target"].forEach(function (direction) {
            var classDefinition = classDefinitionByName[direction];

            if (!classDefinition.fieldDescriptors)
              classDefinition.fieldDescriptors = [];

            var associationFieldName = me[direction + 'Name']; //sourceName or targetName by direction

            var oppositeDirection = (direction == "source" ? "target" : "source");
            var associationFieldType = classDefinitionByName[oppositeDirection].name //class that opposite side

            if (!associationFieldName)
              // associationFieldName = associationFieldType.substring(0, 1).toLowerCase() + associationFieldType.substring(1);

            var collectionClass = null;

            var annotationPartOpposite = "One";
            var annotationPart = "One"

            if ("1 or 0".indexOf(me.value[oppositeDirection + "Multiplicity"]) == -1) { //opposite side is multiple
              associationFieldType = "List<" + associationFieldType + ">";
              associationFieldName = associationFieldName + "List";
              collectionClass = associationFieldType;
              annotationPartOpposite = "Many";
            }

            if ("1 or 0".indexOf(me.value[direction + "Multiplicity"]) == -1) {
              annotationPart = "Many"
            }

            var associationField = null;

            for (var i = 0; i < classDefinition.fieldDescriptors.length; i++) {
              var fieldDescriptor = classDefinition.fieldDescriptors[i];
              //fieldDescriptor._aggregation = null;
              if (fieldDescriptor.name == associationFieldName || (me.relationView && fieldDescriptor._aggregationId == me.relationView.id)) {
                associationField = classDefinition.fieldDescriptors[i];
                break;
              }
            }

            if (!associationField) {
              associationField = {_aggregationId: me.relationView ? me.relationView.id : null};
              classDefinition.fieldDescriptors.push(associationField);
            }

            associationField.className = associationFieldType;
            associationField.name = associationFieldName;
            associationField.collectionClass = collectionClass;

            if (!associationField.attributes)
              associationField.attributes = {};

            associationField.attributes['relationAnnotation'] = annotationPart + "To" + annotationPartOpposite;
            associationFields[direction] = associationField;

          });

          //mapped by option
          ["source", "target"].forEach(function (direction) {
            var associationField = associationFields[direction];
            var oppositeDirection = (direction == "source" ? "target" : "source");
            var oppositeAssociationField = associationFields[oppositeDirection];

            var relation = (associationField.attributes['relationAnnotation']);

            if (relation.indexOf("Many") == relation.length - 4) {
              associationField.attributes['relationAnnotation.mappedBy'] = oppositeAssociationField.name;
            }
          })
        }
      }
    }
  }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
