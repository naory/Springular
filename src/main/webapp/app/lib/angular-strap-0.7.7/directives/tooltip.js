'use strict';

angular.module('$strap.directives')

.directive('bsTooltip', function($parse, $compile) {

  var name = !!$.fn.emulateTransitionEnd ? 'bs.tooltip' : 'tooltip';

  return {
    restrict: 'A',
    scope: true,
    link: function postLink(scope, element, attrs, ctrl) {

      var getter = $parse(attrs.bsTooltip),
        setter = getter.assign,
        value = getter(scope);

      // Watch bsTooltip for changes
      scope.$watch(attrs.bsTooltip, function(newValue, oldValue) {
        value = newValue;
      });

      if(!!attrs.unique) {
        element.on('show', function(ev) {
          // Hide any active popover except self
          $('.tooltip.in').each(function() {
            var $this = $(this),
              tooltip = $this.data(name);
            if(tooltip && !tooltip.$element.is(element)) {
              $this.tooltip('hide');
            }
          });
        });
      }

      // Initialize tooltip
      element.tooltip({
        title: function() { return angular.isFunction(value) ? value.apply(null, arguments) : value; },
        html: true
      });

      // Bootstrap override to provide events & tip() reference
      var tooltip = element.data(name);
      tooltip.show = function() {
        var r = $.fn.tooltip.Constructor.prototype.show.apply(this, arguments);
        // Bind tooltip to the tip()
        this.tip().data(name, this);
        return r;
      };

      //Provide scope display functions
      scope._tooltip = function(event) {
        element.tooltip(event);
      };
      scope.hide = function() {
        element.tooltip('hide');
      };
      scope.show = function() {
        element.tooltip('show');
      };
      scope.dismiss = scope.hide;

    }
  };

});
