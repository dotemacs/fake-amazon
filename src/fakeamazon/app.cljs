(ns fakeamazon.app
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            ["create-react-class" :as crc]
            ["react-native-progress-steps" :as progress-steps]
            ["react-native-circle-checkbox" :as cb]))

(def safe-area-view (r/adapt-react-class (.-SafeAreaView rn)))
(def view (r/adapt-react-class (.-View rn)))
(def text (r/adapt-react-class (.-Text rn)))
(def textinput (r/adapt-react-class (.-TextInput rn)))
(def scroll-view (r/adapt-react-class (.-ScrollView rn)))
(def picker (r/adapt-react-class (.-Picker rn)))
(def picker-item (r/adapt-react-class (.-Item (.-Picker rn))))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight rn)))
(def image (r/adapt-react-class (.-Image rn)))
(def button (r/adapt-react-class (.-Button rn)))
(def checkbox (r/adapt-react-class (.-default cb)))
(def steps (r/adapt-react-class (.-ProgressSteps progress-steps)))
(def step (r/adapt-react-class (.-ProgressStep progress-steps)))


(def styles
  ^js (-> {:container
           {:flex 1
            :backgroundColor "#fff"
            :alignItems "center"
            :justifyContent "center"}
           :title
           {:fontWeight "bold"
            :fontSize 24
            :color "blue"}}
          (clj->js)
          (rn/StyleSheet.create)))

(def amazon-logo (js/require "../assets/amazon.png"))

(defn hr
  "Draw a line, much like <hr/>"
  []
  [view {:style {:align "center"
                 :borderBottomWidth 2.1
                 :borderBottomColor "#e4e4e4"
                 :width 372
                 :margin 0}}])

(defn root []
  [safe-area-view {:style {:margin-top 10}}
   [:> rn/View {}
    [view {:style {:flexDirection "row"}}
     [:> rn/Text {:style {:fontWeight "bold"
                          :fontSize 30
                          :margin-left 10}} "←"]
     [image {:source amazon-logo
             :style {:flexDirection "row"
                     :alignItems "flex-start"
                     :width 120
                     :height 37
                     :margin-left -5
                     :margin-bottom 10}}]]

    [view {:style {:align-items "center"
                   :flex-direction "column"}}
     [view {:style {:align-self "flex-start"
                    :margin-left 30
                    :margin-bottom -25}}
      [text {} "Shipping"]]
     #_ steps
     [view {:style {:flex 1}}
      [steps {:activeStepIconBorderColor "#FCA200"
              :activeLabelColor "#FCA200"
              :activeStepNumColor "white"
              :activeStepIconColor "#FCA200"
              :completedStepIconColor "#E4E4E4"
              :completedProgressBarColor "#E4E4E4"
              :completedCheckColor "#E4E4E4"
              :disabledStepIconColor "#E4E4E4"}
       [step {:label "First Step"
              :nextBtnText ""}
        [view {:style #js {:alignItems "center"}}]]
       [step {:label "Second Step"}
        [view {:style #js {:alignItems "center"}}]]
       [step {:label "Third Step"}
        [view {:style #js {:alignItems "center"}}]]
       [step {:label "Fourth Step"}
        [view {:style #js {:alignItems "center"}}]]]]

     #_ line
     [view {:style {:margin-top 90
                    :margin-bottom 10}}
      [text {:style {:color "#e4e4e4"
                     :text-align "center"}}
       "Select Shipping Address"]
      [view {:style {:align "center"
                     :borderBottomWidth 2.1
                     :borderBottomColor "#e4e4e4"
                     :width 340
                     :margin 3}}]]]

    [view {:style {:alignItems "center"}}
     [view {:style {:margin 10
                    :width 375
                    :border-width 1.8
                    :border-color "#e4e4e4"}}
      [view {:style {:flex-direction :row}}
       [view {:style {:padding 5
                      :flex-direction :column}}
        [text {:style {:font-weight "bold"}} "John Doe Bowie"]
        [text {} "410 Terry Avenue N."]
        [text {} "Seattle, WA 98109"]
        [text {} "United States"]]
       ;; checkbox
       [view {:style {:alignItems "flex-end"
                      :margin-top 25
                      :margin-left 200}}
        [checkbox {:checked true
                   :on-toggle #()}]]]
      (hr)
      [view {:style {:flex-direction :row}}
       [view {:style {:padding 5
                      :flex-direction :column}}
        [text {:style {:font-weight "bold"}} "Abraham Doe Bowie"]
        [text {} "425 106th Ave NE"]
        [text {} "Bellevue, WA 98033"]
        [text {} "United States"]]

       ;; checkbox
       [view {:style {:alignItems "flex-end"
                      :margin-top 25
                      :margin-left 191}}
        [checkbox {:checked false
                   :outer-color "#e4e4e4"
                   :on-toggle #()}]]]
      (hr)
      [view {:style {:flex-direction :row}}
       [view {:style {:padding-top 30
                      :padding-bottom 30
                      :margin-left 5}}
        [text {} "Add a new address"]]
       [view {:style {:margin-top 20
                      :margin-left 218}}
        [text {:style {:font-size 30}} ">"]]]
      (hr)
      [view {:style {:flex-direction :row}}
       [view {:style {:padding-top 30
                      :padding-bottom 30
                      :margin-left 5}}
        [text {} "Ship to an Amazon pickup location"]]
       [view {:style {:margin-top 20
                      :margin-left 118}}
        [text {:style {:font-size 30}} ">"]]]]]

    #_ buttons
    [touchable-highlight
     {:style {:background-color "#283648"
              :margin-top 10
              :margin-left 30
              :margin-right 30
              :padding 10
              :border-radius 10}
      :on-press #(js/alert "HELLO!")}
     [text {:style {:color "white"
                    :text-align "center"
                    :font-size 15}} "Edit Addresses"]]
    [touchable-highlight
     {:style {:background-color "#FCA200"
              :margin-top 10
              :margin-left 30
              :margin-right 30
              :padding 10
              :border-radius 10}
      :on-press #(js/alert "HELLO!")}
     [text {:style {:color "white"
                    :text-align "center"
                    :font-size 15}} "Continue"]]]])

(defonce root-ref (atom nil))
(defonce root-component-ref (atom nil))

(defn render-root [root]
  (let [first-call? (nil? @root-ref)]
    (reset! root-ref root)

    (if-not first-call?
      (do
        (when-let [root @root-component-ref]
          (.forceUpdate ^js root)))
      (let [Root
            (crc
             #js {:componentDidMount
                  (fn []
                    (this-as this
                      (reset! root-component-ref this)))
                  :componentWillUnmount
                  (fn []
                    (reset! root-component-ref nil))
                  :render
                  (fn []
                    (let [body @root-ref]
                      (if (fn? body)
                        (body)
                        body)))})]

        (rn/AppRegistry.registerComponent "FakeAmazon" (fn [] Root))))))

(defn start
  {:dev/after-load true}
  []
  (render-root (r/as-element [root])))

(defn init []
  (start))
