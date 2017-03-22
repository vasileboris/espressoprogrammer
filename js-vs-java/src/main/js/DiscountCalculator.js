(function() {
    'use strict';

    function calculator(goodsType) {
        var discount = 0.1;
        if('FOOD' === goodsType) {
            discount = 0.2;
        }
        return function(value) {
            return value * (1 - discount);
        }
    }

    var beerDiscountCalculator = calculator('BEER');
    var foodDiscountCalculator = calculator('FOOD');

    console.log('FIRST SELL');
    console.log('BEER(100): ' + beerDiscountCalculator(100));
    console.log('FOOD(100): ' + foodDiscountCalculator(100));

    console.log('SECOND SELL');
    console.log('BEER(100): ' + beerDiscountCalculator(100));
    console.log('FOOD(100): ' + foodDiscountCalculator(100));

    console.log('THIRD SELL');
    console.log('BEER(100): ' + beerDiscountCalculator(100));
    console.log('FOOD(100): ' + foodDiscountCalculator(100));

})();