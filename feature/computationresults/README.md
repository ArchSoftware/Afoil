### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :core
    :core:designsystem["designsystem"]
    :core:ui["ui"]
    :core:common["common"]
    :core:data["data"]
    :core:model["model"]
    :core:projectstore["projectstore"]
  end
  subgraph :feature
    :feature:computationresults["computationresults"]
  end
  :feature:computationresults --> :core:designsystem
  :feature:computationresults --> :core:ui
  :feature:computationresults --> :core:common
  :feature:computationresults --> :core:data
  :feature:computationresults --> :core:model
  :feature:computationresults --> :core:projectstore

classDef android-library fill:#3BD482,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :feature:computationresults android-library
class :core:designsystem unknown
class :core:ui unknown
class :core:common unknown
class :core:data unknown
class :core:model unknown
class :core:projectstore unknown

```