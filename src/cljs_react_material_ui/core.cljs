(ns cljs-react-material-ui.core
  (:refer-clojure :exclude [list stepper])
  (:require [cljsjs.material-ui]
            [clojure.walk :refer [postwalk]]
            [sablono.util :refer [camel-case camel-case-keys]]
            [goog.object :as gobject]))

(defn transform-keys [t coll]
  "Recursively transforms all map keys in coll with t."
  (letfn [(transform [[k v]] [(t k) v])]
    (postwalk (fn [x] (if (map? x) (into {} (map transform x)) x)) coll)))

(def props-kebab->camel->js (comp clj->js camel-case-keys))

(defn create-mui-cmp
  ([react-class args]
   (let [first-arg (first args)
         args (if (or (map? first-arg) (nil? first-arg)) args (cons {} args))]
     (apply js/React.createElement react-class
            (props-kebab->camel->js (first args)) (rest args))))
  ([root-obj type args]
   (create-mui-cmp (gobject/getValueByKeys root-obj type) args)))

(defn create-mui-theme
  ([] (create-mui-theme nil))
  ([raw-theme] (->> raw-theme
                 (transform-keys camel-case)
                 clj->js
                 js/MaterialUIStyles.createMuiTheme)))

(defn create-mui-transition [theme properties transitionParams]
  (.create (gobject/get theme "transitions") (clj->js properties) (clj->js transitionParams)))

(defn color
  "Takes the color as a symbol for the first argument and then either
  a number or a symbol for the second arg to specify the shade"
  [color-name color-key]
  (let [key-string (if (integer? color-key)
                     (str color-key)
                     (name (camel-case color-key)))]
    (gobject/getValueByKeys js/MaterialUIColors (name (camel-case color-name)) key-string)))

(def make-selectable (gobject/get js/MaterialUI "makeSelectable"))

(def create-mui-el (partial create-mui-cmp js/MaterialUI))

(defn selectable-list [& args] (create-mui-cmp (make-selectable (gobject/get js/MaterialUI "List")) args))

(defn app-bar [& args] (create-mui-el "AppBar" args))
(defn avatar [& args] (create-mui-el "Avatar" args))
(defn backdrop [& args] (create-mui-el "Backdrop" args))
(defn badge [& args] (create-mui-el "Badge" args))
(defn bottom-navigation [& args] (create-mui-el "BottomNavigation" args))
(defn bottom-navigation-action [& args] (create-mui-el "BottomNavigationAction" args))
(defn button [& args] (create-mui-el "Button" args))
(defn button-base [& args] (create-mui-el "ButtonBase" args))
(defn card [& args] (create-mui-el "Card" args))
(defn card-actions [& args] (create-mui-el "CardActions" args))
(defn card-content [& args] (create-mui-el "CardContent" args))
(defn card-header [& args] (create-mui-el "CardHeader" args))
(defn card-media [& args] (create-mui-el "CardMedia" args))
(defn checkbox [& args] (create-mui-el "Checkbox" args))
(defn chip [& args] (create-mui-el "Chip" args))
(defn circular-progress [& args] (create-mui-el "CircularProgress" args))
(defn click-away-listener [& args] (create-mui-el "ClickAwayListener" args))
(defn collapse [& args] (create-mui-el "Collapse" args))
(defn css-baseline [& args] (create-mui-el "CssBaseline" args))
(defn dialog [& args] (create-mui-el "Dialog" args))
(defn dialog-actions [& args] (create-mui-el "DialogActions" args))
(defn dialog-content [& args] (create-mui-el "DialogContent" args))
(defn dialog-content-text [& args] (create-mui-el "DialogContentText" args))
(defn dialog-title [& args] (create-mui-el "DialogTitle" args))
(defn divider [& args] (create-mui-el "Divider" args))
(defn drawer [& args] (create-mui-el "Drawer" args))
(defn expansion-panel [& args] (create-mui-el "ExpansionPanel" args))
(defn expansion-panel-actions [& args] (create-mui-el "ExpansionPanelActions" args))
(defn expansion-panel-details [& args] (create-mui-el "ExpansionPanelDetails" args))
(defn expansion-panel-summary [& args] (create-mui-el "ExpansionPanelSummary" args))
(defn fade [& args] (create-mui-el "Fade" args))
(defn form-control [& args] (create-mui-el "FormControl" args))
(defn form-control-label [& args] (create-mui-el "FormControlLabel" args))
;(defn grid-list [& args] (create-mui-el "GridList" args))
(defn form-group [& args] (create-mui-el "FormGroup" args))
(defn form-helper-text [& args] (create-mui-el "FormHelperText" args))
(defn form-label [& args] (create-mui-el "FormLabel" args))
(defn grid [& args] (create-mui-el "Grid" args))
(defn grid-list [& args] (create-mui-el "GridList" args))
(defn grid-list-tile [& args] (create-mui-el "GridListTile" args))
(defn grid-list-tile-bar [& args] (create-mui-el "GridListTileBar" args))
(defn grow [& args] (create-mui-el "Grow" args))
(defn hidden [& args] (create-mui-el "Hidden" args))
(defn icon [& args] (create-mui-el "Icon" args))
(defn icon-button [& args] (create-mui-el "IconButton" args))
(defn input [& args] (create-mui-el "Input" args))
(defn input-adornment [& args] (create-mui-el "InputAdornment" args))
(defn input-label [& args] (create-mui-el "InputLabel" args))
(defn linear-progress [& args] (create-mui-el "LinearProgress" args))
(defn list [& args] (create-mui-el "List" args))
(defn list-item [& args] (create-mui-el "ListItem" args))
(defn list-item-avatar [& args] (create-mui-el "ListItemAvatar" args))
(defn list-item-icon [& args] (create-mui-el "ListItemIcon" args))
(defn list-item-secondary-action [& args] (create-mui-el "ListItemSecondaryAction" args))
(defn list-item-text [& args] (create-mui-el "ListItemText" args))
(defn list-subheader [& args] (create-mui-el "ListSubheader" args))
(defn menu [& args] (create-mui-el "Menu" args))
(defn menu-item [& args] (create-mui-el "MenuItem" args))
(defn menu-list [& args] (create-mui-el "MenuList" args))
(defn mobile-stepper [& args] (create-mui-el "MobileStepper" args))
(defn modal [& args] (create-mui-el "Modal" args))
(defn mui-theme-provider [& args] (create-mui-el "MuiThemeProvider" args))
(defn paper [& args] (create-mui-el "Paper" args))
(defn popover [& args] (create-mui-el "Popover" args))
(defn portal [& args] (create-mui-el "Portal" args))
(defn radio [& args] (create-mui-el "Radio" args))
(defn radio-group [& args] (create-mui-el "RadioGroup" args))
(defn root-ref [& args] (create-mui-el "RootRef" args))
(defn select [& args] (create-mui-el "Select" args))
(defn slide [& args] (create-mui-el "Slide" args))
(defn snackbar [& args] (create-mui-el "Snackbar" args))
(defn snackbar-content [& args] (create-mui-el "SnackbarContent" args))
(defn step [& args] (create-mui-el "Step" args))
(defn step-button [& args] (create-mui-el "StepButton" args))
(defn step-connector [& args] (create-mui-el "StepConnector" args))
(defn step-content [& args] (create-mui-el "StepContent" args))
(defn step-icon [& args] (create-mui-el "StepIcon" args))
(defn step-label [& args] (create-mui-el "StepLabel" args))
(defn stepper [& args] (create-mui-el "Stepper" args))
(defn svg-icon [& args] (create-mui-el "SvgIcon" args))
(defn swipeable-drawer [& args] (create-mui-el "SwipeableDrawer" args))
(defn switch [& args] (create-mui-el "Switch" args))
(defn switch-base [& args] (create-mui-el "SwitchBase" args))
(defn tab [& args] (create-mui-el "Tab" args))
(defn table [& args] (create-mui-el "Table" args))
(defn table-body [& args] (create-mui-el "TableBody" args))
(defn table-cell [& args] (create-mui-el "TableCell" args))
(defn table-footer [& args] (create-mui-el "TableFooter" args))
(defn table-head [& args] (create-mui-el "TableHead" args))
(defn table-pagination [& args] (create-mui-el "TablePagination" args))
(defn table-row [& args] (create-mui-el "TableRow" args))
(defn table-sort-label [& args] (create-mui-el "TableSortLabel" args))
(defn tabs [& args] (create-mui-el "Tabs" args))
(defn text-field [& args] (create-mui-el "TextField" args))
(defn toolbar [& args] (create-mui-el "Toolbar" args))
(defn tooltip [& args] (create-mui-el "Tooltip" args))
(defn touch-ripple [& args] (create-mui-el "TouchRipple" args))
(defn typography [& args] (create-mui-el "Typography" args))
(defn zoom [& args] (create-mui-el "Zoom" args))
