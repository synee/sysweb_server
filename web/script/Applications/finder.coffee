$ ->
    `
    jQuery.fn.selectText = function(){
        this.find('input').each(function() {
            if($(this).prev().length == 0 || !$(this).prev().hasClass('p_copy')) {
                $('<p class="p_copy" style="position: absolute; z-index: -1;"></p>').insertBefore($(this));
            }
            $(this).prev().html($(this).val());
        });
        var doc = document;
        var element = this[0];
        console.log(this, element);
        if (doc.body.createTextRange) {
            var range = document.body.createTextRange();
            range.moveToElementText(element);
            range.select();
        } else if (window.getSelection) {
            var selection = window.getSelection();
            var range = document.createRange();
            range.selectNodeContents(element);
            selection.removeAllRanges();
            selection.addRange(range);
        }
    };
    `

    ItemView = Backbone.View.extend {
        initialize: (options)->
            @parent = options.parent
            @$el = options.$el || @$el
            @type = options.type
            if(options.$el)
                @$(".finder-item-name").selectText()
                @model = new Backbone.Model()

        events: {
            "keydown .finder-item-name.new": "_create"
            "blur .finder-item-name.new": "_create"
            "dblclick .finder-item-name:not(.new)": "_editname"
            "blur .finder-item-name:not(.new)": "_rename"
            "keydown .finder-item-name:not(.new)": "_rename"
            "click .finder-icon.dir": (e)-> @parent._itemclick(e, @model.toJSON())
        }

        _editname: (evt)->
            @$(".finder-item-name").attr({contenteditable: true}).focus()
            @$(".finder-item-name").selectText()

        _rename: (evt)->
            self = @
            if !evt.keyCode || evt.keyCode == 13
                source = @model.get("absolutePath")
                dest = source.substr(0, source.lastIndexOf("/") + 1) + @$(".finder-item-name").text()
                if source == dest
                    self.$(".finder-item-name").removeAttr("contenteditable")
                    return
                Sysweb.fs.mv(source, dest).done((result)->
                    if(result.dest.exists)
                        self.$(".finder-item-name").removeAttr("contenteditable")
                        self.model.set(result.dest)
                ).fail((error)->
                    self.$(".finder-item-name").removeAttr("contenteditable")
                    if(error.status == 404)
                        if(window.confirm("Dest name is exists"))
                            self.$(".finder-item-name").text(source.substr(source.lastIndexOf("/") + 1))
                        else
                            self.$(".finder-item-name").focus()
                )
                return false

        _create: (evt)->
            self = @
            console.log(evt)
            if (@model.get("exists")) then return
            if (!evt.keyCode || evt.keyCode == 13) && !@model.get("exists")
                Sysweb.fs.stat(self.parent.currentDir + $(evt.target).text()).done((stat)->
                    if(stat.exists)
                        if(window.confirm("What name you new is exists"))
                            self.$el.remove()
                        else
                            self.$(".finder-item-name").focus()
                ).fail( (error)->
                    if(error.status == 404)
                        Sysweb.fs[self.type](self.parent.currentDir + $(evt.target).text()).done((result)->
                            if(result)
                                self.model.set(result)
                                self.$(".finder-item-name").click((evt)-> self.parent._itemclick(evt, result))
                                self.$(".finder-item-name").removeAttr("contenteditable").removeClass("new")
                        )
                )
                return false

        render: ->
            $item = $("""<span class='finder-item'>
                            <span class='finder-icon #{if @model.toJSON().file then "file" else "dir"}' />
                            <span class='finder-item-name'>#{@model.toJSON().name}</span>
                         </span>
                      """);
            @setElement($item)
            @
    }
    Applications = Sysweb.Applications
    Terminal = Applications.get("terminal")
    Applications.set "finder", Backbone.View.extend

        template: (model)->
            """
            <div class='application-finder'>
                <div class='topbar'>
                    <span class='action action-group'>
                        <span class='action-group-name'>新建</span>
                        <ul>
                            <li><span class='newfile action'>文件</span></li>
                            <li><span class='newdir action'>文件夹</span></li>
                        </ul>
                    </span>
                    <span class='return action'>返回</span>
                </div>
                <div class='container'></div>
            </div>
            """

        elcss:
            position: "absolute",
            width: "80%",
            height: "80%",
            top: "10%",
            left: "10%",
            background: "#fff"

        events: {
            "click .newfile": "_newfile"
            "click .newdir": "_newdir"
            "click .return": "_return"
        }

        _newfile: (e)->
            @createFile("touch")

        _newdir: (e)->
            @createFile("mkdir")

        createFile: (type)->
            self = @

            $item = $("<span class='finder-item'>
                          <span class='finder-icon #{if type == "touch" then "file" else "dir"}' />
                          <span class='finder-item-name new' contenteditable='true'>#{if type == "touch" then "新建文件" else "新建文件夹"}</span>
                       </span>");
            $item.appendTo(@$(".container"));
            new ItemView({$el: $item, parent: @, type: type})

        _return: (e)->
            dir = if (@currentDir != "/" && @currentDir.lastIndexOf("/") == @currentDir.length - 1) then @currentDir.substr(0, @currentDir.length-1) else @currentDir
            @currentDir = dir.substr(0, dir.lastIndexOf("/") + 1)
            @loadChildren()

        initialize: (options) ->
            @currentDir = options.path || "/"

            escFn =  ()->
                @$el.remove();
                KeyBoardMaps.remove("esc", escFn, this);
                Terminal.getTerminal().goon();
            KeyBoardMaps.register("esc", escFn, this);
            @render()

        render: ()->
            @setElement($(@template({})))
            @$el.css(@elcss);
            $("body").append(@$el)
            @loadChildren();
            @

        $: ()->
            return @$el.find.apply(@$el, arguments);
        _itemclick: (e, itemObj)->
            if (itemObj.directory)
                @currentDir += itemObj.name + "/";
                @currentDir = @currentDir.replace("//", "/") while @currentDir.indexOf("//") >= 0
                @loadChildren()

        itemRender: (itemObj)->
            self = @

            new ItemView({model: itemObj, parent: @}).render().$el.appendTo(@$(".container"))
#            $item.appendTo(@$(".container")).click((e)-> self._itemclick(e, itemObj));

        getOpreateDir: (path) ->
            return Terminal.prototype.getOpreateDir.apply(this, [path]);

        loadChildren: ()->
            @currentDir = if (@currentDir.lastIndexOf("/") == @currentDir.length - 1) then @currentDir else @currentDir + "/"
            self = this
            path = @getOpreateDir(@currentDir);
            Sysweb.fs.ls(path).done (result)->
                self.collection = new Backbone.Collection(result)
                self.$(".finder-item").remove();
                self.collection.forEach((model)->
                    self.itemRender(model)
                )




    Terminal.addCommandFunction("finder", (line, args, path)->
        path = path || @currentDir;
        Findbox = Applications.get("finder");
        new Findbox({path: @getOpreateDir(path)});

    )

