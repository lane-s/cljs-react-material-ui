# cljs-react-material-ui

This library is interop to get [Material-UI](https://v1-4-0.material-ui.com/) working in Clojurescript.

Current Material-UI version: `1.4.0-0`

## Installation
- Add `[cljs-react-material-ui "1.4.0-0"]` to your dependencies

## Usage

  ```clojure
  (ns cljs-react-material-ui-example.core
    (:require [cljsjs.material-ui]  ; I recommend adding this at the beginning of core file
                                    ;  so React is always loaded first. It's not always needed
              [cljs-react-material-ui.core :as ui]
              [cljs-react-material-ui.icons :as ic]))   ; SVG icons that comes with MaterialUI
                                                        ; Including icons is not required
  ```

You must start your MaterialUI component tree with ui/mui-theme-provider, which must have exactly one direct child and defined theme. Use the same pattern when you want to change theme for some children, see example app.
```clojure
(ui/mui-theme-provider
    {:theme (ui/create-mui-theme)}
    (ui/paper "Hello world"))
    
(ui/mui-theme-provider 
    {:mui-theme (ui/create-mui-theme 
        {:palette {}})}
    (ui/button
        {:label   "Click me"
         :primary true}))
         
(ui/mui-theme-provider
    {:theme (ui/create-mui-theme (aget js/MaterialUIStyles "DarkRawTheme"))}
    (ui/paper "Hello dark world"))
```

You can use all components (icons also) in their kebab-case form. Either with props or without.
```clojure
(ui/radio-button
    {:value          "some_val"
     :children       "Yes"
     :class-name     "my-radio-class"
     :checked-icon   (ic/action-favorite)
     :unchecked-icon (ic/action-favorite-border)})
     
 (ui/table-row
    (ui/table-header-column "Name")
    (ui/table-header-column "Date"))
```

##### Global objects
```clojure
js/MaterialUI ; Contains constructors to all components. No need to use directly.
js/MaterialUIStyles ; Contains everything from material-ui/src/styles/index.js
js/MaterialUISvgIcons ; Contains constructors to all icons. Exists only when you
                      ; include icons in your code. No need to use directly.
js/MaterialUIUtils ; Contains some of util functions provided by MaterialUI
js/MaterialUIColors ; Contains all of the Material UI colors. Access using the `color` function.
```

##### Using with Reagent
Works with `reagent "0.6.0-alpha"` and up. So the dependency may be specified like this:

`[reagent "0.7.0"]`

A simple Reagent example is as follows:

```clojure
(ns mui-reagent-example.app
  (:require [reagent.core :as reagent :refer [atom]]
            [cljsjs.material-ui]
            [material-ui-icons]
            [cljs-react-material-ui.core :refer [create-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]))

(defonce app-state (atom {:text "Material UI Example"}))

(def base-theme
  (create-mui-theme {:palette {:primary {:main (color :light-blue 700)}
                            :secondary {:main (color :teal :A100)}
                            :text-color (color :common :white)}}))

(defn root []
  [ui/mui-theme-provider
   {:theme base-theme}
   [ui/css-baseline]
   [ui/app-bar
    {:position "static"}
    [ui/toolbar
     [ui/icon-button
      {:color "inherit"
       :style {:margin-left "-20px"
               :margin-right "20px"}}
      [ic/chevron-right]]
     [ui/typography
      {:variant "title"
       :color "inherit"
       :style {:flex-grow "1"}}
      [@app-state :text]]
     [:div
      [ui/icon-button
       {:color "inherit"}
       [ic/account-circle]]]]]])

(reagent/render-component [alt-root]
                          (. js/document (getElementById "app")))
```
&nbsp;
##### Using with Rum
&nbsp;
```clojure
(ns crmui-rum.core
  (:require
    [cljs-react-material-ui.core :refer [create-mui-theme color]]
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.rum :as ui]
    [rum.core :as rum]))
    
(rum/defc thing1
          []
          [:div "content1"])

(defn home-page []
  (ui/mui-theme-provider
    {:mui-theme (create-mui-theme)}
    [:div
     (ui/app-bar {:icon-element-right (ui/icon-button (ic/action-accessibility))})
     (ui/tabs
       (ui/tab {:label "one"}
                [:div ["hey"
                       (ui/paper "yes")]])
       (ui/tab {:label "two"} (thing1))
       (ui/tab {:label "drei"}
                [:div
                 (ui/paper {} "Ima paper")]))]))
    
```

## Selectable List
This library provides pre-made selectable list, whrereas in MaterialUI has to be created manually.
You can access orig `makeSelectable` function as `cljs-react-material-ui.core/make-selectable`
See example in reagent:
```clojure
(defn selectable-list-example []
  (let [list-item-selected (atom 1)]
    (fn []
      [ui/selectable-list
       {:value @list-item-selected
        :on-change (fn [event value]
               
      (reset! list-item-selected value))}
       [ui/subheader {} "Selectable Contacts"]
       [ui/list-item
        {:value 1
         :primary-text "Brendan Lim"
         :nested-items
         [(r/as-element
            [ui/list-item
             {:value 2
              :key 8
              :primary-text "Grace Ng"}])]}]
       [ui/list-item
        {:value 3
         :primary-text "Kerem Suer"}]
       [ui/list-item
        {:value 4
         :primary-text "Eric Hoffman"}]
       [ui/list-item
        {:value 5
         :primary-text "Raquel Parrado"}]])))
```


## MaterialUI Chip Input
If you feel like using [MaterialUIChipInput](https://github.com/TeamWertarbyte/material-ui-chip-input) all you need to
do is add `[cljsjs/material-ui-chip-input "0.17.0-0"]` (or newer version) into your project.clj. 
And now you can use chip-input according to your favorite framework namespace.
```clojure
(ns my.app
    (:require 
      [cljs-react-material-ui.chip-input.core :refer [chip-input]]
      [cljs-react-material-ui.chip-input.reagent :refer [chip-input]]
      [cljs-react-material-ui.chip-input.rum :refer [chip-input]]))
```


## Troubleshooting
##### Caret moves to the end when editing a text field
This happens due to async rendering of clojurescript react libraries.
Luckily, there is a workaround, which fixes most of use cases: Instead of `:value` prop use `:default-value` e.g:
```clojure
(defn simple-text-field [text]
  (let [text-state (r/atom text)]
    (fn []
      [rui/text-field
       {:id "example"
        :default-value @text-state
        :on-change (fn [e] (reset! text-state (.. e -target -value)))}])))
```
