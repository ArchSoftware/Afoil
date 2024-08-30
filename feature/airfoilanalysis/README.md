### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :core
    :core:testing["testing"]
    :core:designsystem["designsystem"]
    :core:ui["ui"]
    :core:data["data"]
    :core:common["common"]
    :core:model["model"]
    :core:projectstore["projectstore"]
  end
  subgraph :feature
    :feature:airfoilanalysis["airfoilanalysis"]
  end
  :feature:airfoilanalysis --> :core:testing
  :feature:airfoilanalysis --> :core:designsystem
  :feature:airfoilanalysis --> :core:ui
  :feature:airfoilanalysis --> :core:data
  :feature:airfoilanalysis --> :core:common
  :feature:airfoilanalysis --> :core:model
  :feature:airfoilanalysis --> :core:projectstore

classDef android-library fill:#3BD482,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :feature:airfoilanalysis android-library
class :core:testing unknown
class :core:designsystem unknown
class :core:ui unknown
class :core:data unknown
class :core:common unknown
class :core:model unknown
class :core:projectstore unknown

```