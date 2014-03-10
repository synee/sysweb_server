$(->
    $(document).ajaxError((event, request, settings)-> $(document).trigger("ajaxerror", [event, request, settings]) )
)

