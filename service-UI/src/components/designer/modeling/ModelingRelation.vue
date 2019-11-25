<template>
  <div>
    <edge-element
      selectable
      connectable
      deletable
      :id="null"
      :vertices.sync="vertices"
      :from.sync="value.sourceElement.elementView.id"
      :to.sync="value.targetElement.elementView.id"
      :_style.sync="style"
      :label.sync="value.name"
    >
    </edge-element>
  </div>
</template>

<script>
  import Element from './Element'

  export default {
    mixins: [Element],
    name: 'modeling-relation',
    props: {
      value: Object
    },
    computed: {
      defaultStyle() {
        return {}
      },
      type() {
        return 'Relation'
      },
      createNew(from, to, vertices) {
        return {
          sourceElement: from,
          targetElement: to,
          selected: false,
          relationView: {
            style: JSON.stringify({}),
            value: vertices,
          }
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
    watch: {},
    mounted: function () {
    },
    methods: {
    }
  }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>

