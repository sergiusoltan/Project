/**
 * Created with IntelliJ IDEA.
 * User: ZIG
 * Date: 13.06.2014
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
'use strict';

angular
    .module('mainApp')
    .controller('ContactCtrl', ['$scope', '$location', 'AuthFactory', 'ContactService', '$modal', 'EvaluationService', function ($scope, $location, AuthFactory, ContactService, $modal, EvaluationService) {

        init();
        $scope.contact = {};
        $scope.recomendedBy = {};
        $scope.evaluations = [];

        function init(){
            $scope.id = Number($location.path().match(/\/contacts\/(\d+)$/)[1]);
            ContactService.getContact($scope.id).then(function(success){
                $scope.contact = success;
                if($scope.contact.recomendedBy){
                    $scope.recomendedBy = JSON.parse($scope.contact.recomendedBy);
                }
            }, function(err){
                alert(err);
            });

            EvaluationService.getAllEvaluations($scope.id).then(function(success){
                $scope.evaluations = success;
                createData();
            }, function(error){
                console.log("failed to load evaluations with error: "+error);
            });
        }

        $scope.openImage = function (url) {
            var modalInstance = $modal.open({
                templateUrl: 'imageFullSized.html',
                controller: 'UploadController',
                size: 'sm',
                resolve: {
                    title: function () {
                        return url;
                    }
                }
            });
        };

        $scope.addEvaluation = function () {
            var modalInstance = $modal.open({
                templateUrl: 'evaluationTemplate.html',
                controller: 'ModalPeopleCtrl',
                size: '',
                resolve: {
                    items: function () {
                        return null;
                    },
                    item: function () {
                        return {};
                    },
                    title: function () {
                        return $scope.contact.name;
                    }
                }
            });

            modalInstance.result.then(function (item) {
                EvaluationService.saveEvaluation($scope.id, item.instance).then(function (success) {
                    $scope.evaluations = success;
                }, function (error) {
                    console.log('failed to save evaluation with ' + error);
                });
            }, function () {
                console.log('modal dismissed');
            });
        };

        $scope.editContact = function () {
            var modalInstance = $modal.open({
                templateUrl: 'updateContactInfo.html',
                controller: 'ModalPeopleCtrl',
                size: '',
                resolve: {
                    items: function () {
                        return null;
                    },
                    item: function () {
                        $scope.contact.withUpdate = true;
                        return $scope.contact;
                    },
                    title: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function (item) {
                if(item.file){
                    ContactService.createUploadUrl().then(function(url){
                        item.uploadUrl = url;
                        ContactService.uploadWithImage(item, item.file).then(function(success){
                            ContactService.getContact($scope.id).then(function(success){
                                $scope.member = success;
                            }, function(error){
                                console.log("failed to get member with" + error );
                            });
                        });
                    });
                    return;
                }

                ContactService.updateInfo(item.instance).then(function (success) {
                    ContactService.getContact($scope.id).then(function(success){
                        $scope.contact = success;
                    }, function(error){
                        console.log("failed to get member with" + error );
                    });
                }, function (error) {
                    console.log('failed to update member with ' + error);
                });
            }, function () {
                console.log('modal dismissed');
            });
        };

        function createData(){
            var data = new google.visualization.DataTable();
            data.addColumn('date', 'Date');

            data.addColumn('number', 'Weight');
            data.addColumn('string', 'Weight title');

            data.addColumn('number', 'Fat percent');
            data.addColumn('string', 'Fat percent title');

            data.addColumn('number', 'Muscle Mass');
            data.addColumn('string', 'Muscle Mass title');

            data.addColumn('number', 'IMC');
            data.addColumn('string', 'IMC title');

            data.addColumn('number', 'Mineralization');
            data.addColumn('string', 'Mineralization title');

            data.addColumn('number', 'Metabolic age');
            data.addColumn('string', 'Metabolic age title');

            data.addColumn('number', 'Hydration');
            data.addColumn('string', 'Hydration title');

            data.addColumn('number', 'Visceral fat');
            data.addColumn('string', 'Visceral title');

            var rows = [];
            $.each($scope.evaluations,function(index, value){
                rows.push([new Date(Number(value.dateYear), Number(value.dateMonth), Number(value.dateDay)),
                    value.weight, 'Weight ' + value.weight + ' kg',
                    value.fat, 'Fat ' + value.fat + '%',
                    value.muscle, 'Muscle mass ' + value.muscle + ' kg',
                    value.imc, 'IMC ' + value.imc,
                    value.mineralization, 'Mineralization ' + value.mineralization,
                    value.metabolicage, 'Metabolic age ' + value.metabolicage + ' years old',
                    value.hydration, 'Hydration ' + value.hydration + '%',
                    value.visceralfat, 'Visceral Fat ' + value.visceralfat + ' kg'])
            });
            data.addRows(rows);

            var options = {
                displayAnnotations: true,
                displayAnnotationsFilter:true,
                thickness:2
            };

            $scope.monitor = {data:data,options:options};
        }

    }]);